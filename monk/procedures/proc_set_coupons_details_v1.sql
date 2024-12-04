CREATE DEFINER=`root`@`localhost` PROCEDURE `proc_set_coupons_details_v1`(
    IN  in_coupon_id           BIGINT(11),
    IN  in_coupon_name         VARCHAR(100),
    IN  in_coupon_type         VARCHAR(100),
    IN  in_coupon_code         VARCHAR(100),
    IN  in_required_amount     FLOAT,
    IN  in_discount_percent    FLOAT,
    IN  in_threshold_limit     INT(4),
    IN  in_buy_products        VARCHAR(250),
    IN  in_buy_limit           INT(4),
    IN  in_get_limit           INT(4),
    IN  in_get_products        VARCHAR(250),
    IN  in_expiry_date         TIMESTAMP,
    IN  in_products            VARCHAR(250),
    IN  in_created_time        TIMESTAMP,
    IN  in_created_by          VARCHAR(50),
    IN  in_reason              VARCHAR(250),
    OUT out_status             INT(1),
    OUT out_error_msg          TEXT
)
BEGIN

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- Capture the error message
        GET DIAGNOSTICS CONDITION 1 
            out_error_msg = MESSAGE_TEXT;
        SET out_error_msg = CONCAT('Error in proc_set_coupons_details_v1: ', out_error_msg);
        
        -- Set the status to failure
        SET out_status = 0;
        
        -- Rollback the transaction
        ROLLBACK;
    END;

    -- Initialize output variables
    SET out_status = 0;
    SET out_error_msg = '';

    -- Start transaction
    START TRANSACTION;

    -- Insert or Update coupon data using ON DUPLICATE KEY UPDATE
    INSERT INTO tbl_coupons_data (
        c_id, c_coupon_name, c_coupon_code, c_coupon_type, c_required_amount,
        c_discount_percent, c_threshold_limit, c_buy_products, c_buy_limit,
        c_get_limit, c_get_products, c_created_time, c_created_by, c_reason,
        c_products, c_expiry_date
    ) VALUES (
        in_coupon_id, in_coupon_name, in_coupon_code, in_coupon_type, in_required_amount,
        in_discount_percent, in_threshold_limit, in_buy_products, in_buy_limit,
        in_get_limit, in_get_products, in_created_time, in_created_by, in_reason,
        in_products, in_expiry_date
    )
    ON DUPLICATE KEY UPDATE
        c_coupon_name = in_coupon_name,
        c_coupon_type = in_coupon_type,
        c_required_amount = in_required_amount,
        c_discount_percent = in_discount_percent,
        c_threshold_limit = in_threshold_limit,
        c_buy_products = in_buy_products,
        c_buy_limit = in_buy_limit,
        c_get_limit = in_get_limit,
        c_get_products = in_get_products,
        c_created_time = in_created_time,
        c_created_by = in_created_by,
        c_reason = in_reason,
        c_products = in_products,
        c_expiry_date = in_expiry_date;

    -- Insert into log table for tracking changes
    INSERT INTO tbl_coupons_data_log (
		c_coupon_id,c_coupon_name, c_coupon_code, c_coupon_type, c_required_amount,
        c_discount_percent, c_threshold_limit, c_buy_products, c_buy_limit, c_get_limit,
        c_get_products, c_updated_time, c_updated_by, c_reason, c_products, c_expiry_date
    )
	SELECT 
		c_id, c_coupon_name, c_coupon_code, c_coupon_type, c_required_amount,
		c_discount_percent, c_threshold_limit, c_buy_products, c_buy_limit, c_get_limit,
		c_get_products, c_created_time, c_created_by, c_reason, c_products, c_expiry_date
	FROM tbl_coupons_data
	WHERE c_coupon_code = in_coupon_code;

    -- Set status to success
    SET out_status = 1;

    -- Commit the transaction
    COMMIT;

END;