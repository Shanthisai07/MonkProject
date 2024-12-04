DELIMITER $$
USE `monk`$$
CREATE DEFINER=`root`@`localhost` FUNCTION `func_apply_discount_from_lists`(
	in_cart_products 		VARCHAR(250),
    in_cart_product_prices 	VARCHAR(250),
    in_eligible_products	VARCHAR(250),
    in_discount_percent		FLOAT
   ) RETURNS float
    DETERMINISTIC
BEGIN
	DECLARE v_product 									VARCHAR(250);
    DECLARE v_price,v_discounted_price					FLOAT;
    DECLARE v_product_start, v_price_start 				INT DEFAULT 1;
    DECLARE v_product_end, v_price_end, v_comma_position INT;
    DECLARE v_total_discounted_amount					FLOAT DEFAULT 0;
    
    -- Loop through the products list and corresponding prices list
	WHILE v_product_start > 0 DO
		
        -- Find the position of the next comma in the products list
		SET v_comma_position = LOCATE(",", in_cart_products, v_product_start);
        
        -- If there are no more commas, set the end of the string
        IF v_comma_position = 0 THEN
			SET v_product_end = LENGTH(in_cart_products)+1;  -- to fetch last product & price
            SET v_price_end	= LENGTH(in_cart_product_prices)+1;
		ELSE 
			SET v_product_end = v_comma_position;
            SET v_price_end = LOCATE(",", in_cart_product_prices, v_price_start);
		END IF;
        
        SET v_product = SUBSTRING(in_cart_products, v_product_start, v_product_end - v_product_start); -- current product
        SET v_price = CAST(SUBSTRING(in_cart_product_prices, v_price_start, v_price_end - v_price_start) AS DECIMAL(10,2)); -- casting corresponding price to float
        
        -- checking if cart product is in eligible products
        IF(FIND_IN_SET(v_product, in_eligible_products) > 0) THEN
			SET v_discounted_price = v_price - (v_price*in_discount_percent)/100 ;  -- applying discount 
            SET v_total_discounted_amount = v_total_discounted_amount + v_discounted_price;
		ELSE 
			SET v_total_discounted_amount = v_total_discounted_amount + v_price; -- adding full price
		END IF;
        
        -- move to next product & price in respective lists
        SET v_product_start = v_product_end + 1; 
        SET v_price_start = v_price_end + 1;
        
        -- terminating loop if there are no commas left by setting v_product_start as 0
        IF v_comma_position = 0 THEN
            SET v_product_start = 0;
        END IF;
		
	END WHILE;
    
    RETURN v_total_discounted_amount;  -- return discounted amount
END$$

DELIMITER ;
