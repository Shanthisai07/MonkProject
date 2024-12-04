USE `monk`;
DROP procedure IF EXISTS `proc_apply_coupon_v1`;

DELIMITER $$
USE `monk`$$
CREATE PROCEDURE `proc_apply_coupon_v1` (
    IN in_user_id BIGINT(11),
    IN in_cart_amount FLOAT,
    IN in_cart_products VARCHAR(250),
    IN in_cart_each_product_price VARCHAR(250),
    IN in_coupon_code VARCHAR(250),
    OUT out_amount FLOAT,
    OUT out_claim_limit INT,
    OUT out_status INT,
    OUT out_error_msg TEXT
)
proc_apply_coupon_v1 : BEGIN
    DECLARE v_coupon_id BIGINT(11);
    DECLARE v_coupon_threshold_limit, v_buy_limit, v_get_limit  INT DEFAULT 0;
    DECLARE v_coupon_type, v_products, v_buy_products, v_get_products VARCHAR(250) DEFAULT NULL;
    DECLARE v_required_amount, v_discount_percent FLOAT;
    DECLARE v_expiry_date TIMESTAMP;    
    DECLARE v_user_threshold_limit INT DEFAULT NULL;
    DECLARE v_buy_products_check_response, v_get_products_check_response INT DEFAULT 0;

    -- Error handling
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 
        out_error_msg = MESSAGE_TEXT;
        SET out_error_msg = CONCAT('Error in proc_apply_coupon_v1: ', out_error_msg);
        SET out_status = 0;
        ROLLBACK;
    END;

    -- Start transaction
    START TRANSACTION;

    -- Fetch coupon details
    SELECT c_id, c_coupon_type, c_discount_percent, c_required_amount, c_buy_products, c_get_products, c_buy_limit, c_get_limit, c_products, c_expiry_date, c_threshold_limit INTO v_coupon_id, v_coupon_type, v_discount_percent, v_required_amount, v_buy_products, v_get_products, v_buy_limit, v_get_limit, v_products, v_expiry_date, v_coupon_threshold_limit FROM tbl_coupons_data WHERE c_coupon_code = in_coupon_code;

    	-- Check if coupon is expired
    
	IF v_coupon_id IS NOT NULL THEN
		IF v_expiry_date < NOW() THEN
			SET out_status = 8; -- Coupon expired
			SET out_error_msg = 'Coupon has expired.';
			ROLLBACK;
			LEAVE proc_apply_coupon_v1; -- Exit procedure
		ELSE
			-- Check if the user has exceeded threshold
			SELECT c_threshold_limit 
			INTO v_user_threshold_limit
			FROM tbl_user_coupons_history
			WHERE c_user_id = in_user_id AND c_coupon_id = v_coupon_id;

			IF v_user_threshold_limit IS NULL THEN
			    -- If the record does not exist, initialize the threshold limit (assuming you want to start with a new limit)
			    SET v_user_threshold_limit = v_coupon_threshold_limit;
			END IF;
			
			-- Now proceed with the existing check for threshold limit
			IF v_user_threshold_limit <= 0 THEN
				SET out_status = 9; -- Threshold limit exceeded
				SET out_error_msg = 'Threshold limit exceeded for this coupon.';
				ROLLBACK;
				LEAVE proc_apply_coupon_v1; -- Exit procedure
			ELSE
				-- If coupon type is cart-wise
				IF v_coupon_type = 'Cart_wise' THEN
					IF v_required_amount <= in_cart_amount THEN
						SET out_amount = in_cart_amount - (in_cart_amount * v_discount_percent) / 100;
						SET out_status = 1; -- Success
					ELSE
						SET out_status = 10; -- Cart total amount not enough
						SET out_error_msg = CONCAT('To apply coupon, cart total amount must exceed ', v_required_amount);
						ROLLBACK;
						LEAVE proc_apply_coupon_v1; -- Exit procedure
					END IF;

					-- If coupon type is product-wise
				ELSEIF v_coupon_type = 'Product_wise' THEN
					-- Assuming `func_apply_discount_from_lists` LEAVEs the discount amount
					SELECT func_apply_discount_from_lists(in_cart_products, in_cart_each_product_price, v_products, v_discount_percent)
					INTO out_amount;

					IF out_amount = 0 THEN
						SET out_status = 11; -- No eligible products
						SET out_error_msg = CONCAT('To apply coupon, please add products from ', v_products);
						ROLLBACK;
						LEAVE proc_apply_coupon_v1; -- Exit procedure
					ELSE
						SET out_status = 1; -- Success
					END IF;

					-- If coupon type is Buy X Get Y
				ELSEIF v_coupon_type = 'BxGy' THEN
					-- Check if the required products are present in the cart
					SET out_amount = in_cart_amount;
					SELECT func_find_product_in_products_list(in_cart_products, v_buy_products, v_buy_limit, 0)
					INTO v_buy_products_check_response;

					IF v_buy_products_check_response > 0 THEN
						SELECT func_find_product_in_products_list(in_cart_products, v_get_products, v_get_limit, 1)
						INTO v_get_products_check_response;

						IF v_get_products_check_response > 0 THEN
							SET out_status = 1; -- Success
						ELSE
							SET out_status = 11; -- No eligible products for "get"
							SET out_error_msg = CONCAT('To apply coupon, please add at least one of these products: ', v_get_products);
							ROLLBACK;
							LEAVE proc_apply_coupon_v1; -- Exit procedure
						END IF;
					ELSE
						SET out_status = 11; -- Insufficient products in cart
						SET out_error_msg = CONCAT('To apply coupon, please add at least ', v_buy_limit, ' products from ', v_buy_products);
						ROLLBACK;
						LEAVE proc_apply_coupon_v1; -- Exit procedure
					END IF;
				END IF;
			END IF;
		END IF;
	ELSE
		-- Invalid coupon code
		SET out_status = 4;
		SET out_error_msg = 'Invalid coupon code.';
		ROLLBACK;
		LEAVE proc_apply_coupon_v1; -- Exit procedure
	END IF;

    -- If coupon is valid, update user history and log
    IF out_status = 1 THEN
        IF v_user_threshold_limit = v_coupon_threshold_limit THEN
            SET v_user_threshold_limit = v_coupon_threshold_limit - 1;
            INSERT INTO tbl_user_coupons_history (c_user_id, c_coupon_id, c_created_time, c_threshold_limit, c_last_claimed_date)
            VALUES (in_user_id, v_coupon_id, NOW(), v_user_threshold_limit, NOW());
        ELSE
            SET v_user_threshold_limit = v_user_threshold_limit - 1;
            UPDATE tbl_user_coupons_history
            SET c_threshold_limit = v_user_threshold_limit, c_last_claimed_date = NOW()
            WHERE c_user_id = in_user_id AND c_coupon_id = v_coupon_id;
        END IF;
		SET out_claim_limit = v_user_threshold_limit;
        -- Log coupon history
        INSERT INTO tbl_user_coupons_history_log (c_user_id, c_coupon_id, c_created_time, c_threshold_limit, c_last_claimed_date)
        VALUES (in_user_id, v_coupon_id, NOW(), v_user_threshold_limit, NOW());
    ELSE
    	SET out_claim_limit = v_coupon_threshold_limit;
    END IF;
	
    -- Commit transaction if no errors
    COMMIT;

END ;$$

DELIMITER ;

