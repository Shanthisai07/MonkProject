package com.example.monk.requests;


public class ClaimCouponResponse extends BaseResponse {
	
	private Double amountAfterCouponApplied;
	
	private Integer thresholdLimit;

	public Double getAmountAfterCouponApplied() {
		return amountAfterCouponApplied;
	}

	public void setAmountAfterCouponApplied(Double amountAfterCouponApplied) {
		this.amountAfterCouponApplied = amountAfterCouponApplied;
	}

	public Integer getThresholdLimit() {
		return thresholdLimit;
	}

	public void setThresholdLimit(Integer thresholdLimit) {
		this.thresholdLimit = thresholdLimit;
	}

	@Override
	public String toString() {
		return "ClaimCouponResponse [amountAfterCouponApplied=" + amountAfterCouponApplied + ", thresholdLimit="
				+ thresholdLimit + "]";
	}
	
}
