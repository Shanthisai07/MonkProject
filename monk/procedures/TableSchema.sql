use monk;

drop table if exists tbl_coupons_data;
create table tbl_coupons_data(
		c_id             	BIGINT(11)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
		c_coupon_name	 	VARCHAR(100)	NOT NULL,
        c_coupon_code	 	VARCHAR(100)	NOT NULL UNIQUE,
		c_coupon_type	 	VARCHAR(100)	NOT NULL,
        c_required_amount	FLOAT			DEFAULT NULL,
		c_discount_percent	FLOAT			DEFAULT NULL,
		c_threshold_limit	INT(4)			NOT NULL,
		c_buy_products		VARCHAR(250)	DEFAULT NULL,
        c_buy_limit			INT(4)			DEFAULT NULL,
        c_get_limit			INT(4)			DEFAULT NULL,
        c_get_products		VARCHAR(250)	DEFAULT NULL,
		c_created_time	 	TIMESTAMP		NOT NULL,
		c_created_by		VARCHAR(50)		NOT NULL,
		c_reason		 	VARCHAR(250)	DEFAULT NULL,
		c_products		 	VARCHAR(250)	DEFAULT NULL,
		c_expiry_date	 	TIMESTAMP		NOT NULL,
	);

drop table if exists tbl_coupons_data_log;

create table tbl_coupons_data_log(
		c_id             	BIGINT(11)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
		c_coupon_id 		BIGINT(11)		NOT NULL,
		c_coupon_name	 	VARCHAR(100)	NOT NULL,
        c_coupon_code	 	VARCHAR(100)	NOT NULL,
		c_coupon_type	 	VARCHAR(100)	NOT NULL,
        c_required_amount	FLOAT			DEFAULT NULL,
		c_discount_percent	FLOAT			DEFAULT NULL,
		c_threshold_limit	INT(4)			NOT NULL,
		c_buy_products		VARCHAR(250)	DEFAULT NULL,
        c_buy_limit			INT(4)			DEFAULT NULL,
        c_get_limit			INT(4)			DEFAULT NULL,
        c_get_products		VARCHAR(250)	DEFAULT NULL,
		c_updated_time	 	TIMESTAMP		NOT NULL,
		c_updated_by		VARCHAR(50)		NOT NULL,
		c_reason		 	VARCHAR(250)	DEFAULT NULL,
		c_products		 	VARCHAR(250)	DEFAULT NULL,
		c_expiry_date	 	TIMESTAMP		NOT NULL
	);

use monk;
drop table if exists tbl_user_coupons_history;
create table tbl_user_coupons_history(
		c_id             	BIGINT(11)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
		c_user_id			BIGINT(11)		NOT NULL,
		c_coupon_id			BIGINT(11)		NOT NULL,
		c_created_time	 	TIMESTAMP		NOT NULL,
		c_threshold_limit	INT(4)			NULL,
        c_last_claimed_date TIMESTAMP		NOT NULL
	);

drop table if exists tbl_user_coupons_history_log;
create table tbl_user_coupons_history_log(
		c_id             	BIGINT(11)		NOT NULL AUTO_INCREMENT PRIMARY KEY,
		c_user_id			BIGINT(11)		NOT NULL,
		c_coupon_id			BIGINT(11)		NOT NULL,
		c_created_time	 	TIMESTAMP		NOT NULL,
		c_threshold_limit	INT(4)			NULL,
        c_last_claimed_date TIMESTAMP		NOT NULL
	);
