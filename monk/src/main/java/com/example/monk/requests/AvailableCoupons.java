package com.example.monk.requests;

import java.sql.Timestamp;

public class AvailableCoupons {
	
	private String couponName;
	
	private Integer couponId;

	private Timestamp expiryDate;
	
	private String couponCode;
	
	private String couponType;
	
	private boolean applicable;
	
	private Integer thresholdLimit;
	
	private String requiredCondition;

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public boolean isApplicable() {
		return applicable;
	}

	public void setApplicable(boolean applicable) {
		this.applicable = applicable;
	}

	public Integer getThresholdLimit() {
		return thresholdLimit;
	}

	public void setThresholdLimit(Integer thresholdLimit) {
		this.thresholdLimit = thresholdLimit;
	}

	public String getRequiredCondition() {
		return requiredCondition;
	}

	public void setRequiredCondition(String requiredCondition) {
		this.requiredCondition = requiredCondition;
	}

	@Override
	public String toString() {
		return "AvailableCoupons [couponName=" + couponName + ", couponId=" + couponId + ", expiryDate=" + expiryDate
				+ ", couponCode=" + couponCode + ", couponType=" + couponType + ", applicable=" + applicable
				+ ", thresholdLimit=" + thresholdLimit + ", requiredCondition=" + requiredCondition + "]";
	}
	
	
}
