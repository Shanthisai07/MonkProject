package com.example.monk.requests;

import java.util.List;

public class ApplyCouponRequest {
	
	private Integer userId;
	
	private Double amount;
	
	private String couponCode;
	
	private List<String> products;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "ApplyCouponRequest [userId=" + userId + ", amount=" + amount + ", couponCode=" + couponCode
				+ ", products=" + products + "]";
	}
	
}
