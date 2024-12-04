use monk;
select database();

delimiter #

drop procedure if exists proc_get_all_coupon_details_v1;

create procedure proc_get_all_coupon_details_v1 (
)
begin
select * from tbl_coupons_data;
end #
delimiter ;
