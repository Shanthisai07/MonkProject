DELIMITER $$

CREATE FUNCTION `func_find_product_in_products_list` (
    in_products          VARCHAR(250), 
    eligible_products    VARCHAR(250),
    products_limit       INTEGER,
    get_check            INTEGER
)
RETURNS INTEGER
DETERMINISTIC
BEGIN
    DECLARE v_product              VARCHAR(250);
    DECLARE v_product_start         INTEGER DEFAULT 1;
    DECLARE v_comma_position, v_product_end, v_threshold_limit INTEGER DEFAULT 0;
    
    -- Start processing each product in the in_products list
    WHILE v_product_start > 0 DO
        
        -- Find the next comma position in the list of products
        SET v_comma_position = LOCATE(",", in_products, v_product_start);
        
        IF v_comma_position = 0 THEN
            SET v_product_end = LENGTH(in_products) + 1;
        ELSE
            SET v_product_end = v_comma_position;
        END IF;
        
        -- Extract the current product from the list
        SET v_product = SUBSTRING(in_products, v_product_start, v_product_end - v_product_start);
        
        -- Check if the product exists in the eligible_products list
        IF FIND_IN_SET(v_product, eligible_products) > 0 THEN
            SET v_threshold_limit = v_threshold_limit + 1;
        END IF;
        
        -- If the comma is at the end of the string, stop the loop
        IF v_comma_position = 0 THEN
            SET v_product_start = 0;
        ELSE
            SET v_product_start = v_comma_position + 1; -- Move to the next product
        END IF;
    END WHILE;

    -- Return 1 if the threshold limit is met for the given condition (get_check)
    IF get_check = 0 THEN
        IF v_threshold_limit >= products_limit THEN
            RETURN 1;
        ELSE
            RETURN 0;
        END IF;
    ELSE
        IF v_threshold_limit >= 1 THEN  -- Check if at least 1 product is found in the eligible list
            RETURN 1;
        ELSE
            RETURN 0;
        END IF;
    END IF;
END $$

DELIMITER ;
