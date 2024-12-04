USE `monk`;
DROP procedure IF EXISTS `proc_get_all_available_coupons_details_v1`;

DELIMITER $$
USE `monk`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `proc_get_all_available_coupons_details_v1`(
	IN in_user_id BIGINT(11)
)
BEGIN
	DECLARE v_count INT DEFAULT 0;
    
    select count(*) into v_count from tbl_user_coupons_history where c_user_id = in_user_id;
    
    if v_count > 0 then
		select data.c_id, data.c_coupon_name, data.c_coupon_type, data.c_coupon_code, data.c_discount_percent, history.c_threshold_limit, data.c_required_amount, data.c_buy_products, data.c_get_products, data.c_buy_limit, data.c_get_limit, data.c_expiry_date, data.c_products, data.c_created_time, data.c_created_by, data.c_reason  from tbl_coupons_data data left join tbl_user_coupons_history history on data.c_id = history.c_coupon_id where data.c_expiry_date > now() and history.c_user_id = in_user_id;
	else
		select * from tbl_coupons_data where c_expiry_date > now();
	end if;
END;$$

DELIMITER ;
