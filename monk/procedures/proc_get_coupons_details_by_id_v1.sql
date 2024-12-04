USE `monk`;
DROP procedure IF EXISTS `monk`.`proc_get_coupons_details_by_id_v1`;

DELIMITER $$
USE `monk`$$
CREATE PROCEDURE `proc_get_coupons_details_by_id_v1`( IN in_coupon_id  BIGINT(11))
BEGIN
select * from tbl_coupons_data where c_id = in_coupon_id;
END$$

DELIMITER ;