package com.example.monk.requests;

import java.util.HashMap;
import java.util.List;

public class ClaimRequest {
	
	private Integer userId;
	
	private String couponCode;
	
	private HashMap<String, Double> cartProducts;
	
	private Double cartAmount;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public HashMap<String, Double> getCartProducts() {
		return cartProducts;
	}

	public void setCartProducts(HashMap<String, Double> cartProducts) {
		this.cartProducts = cartProducts;
	}

	public Double getCartAmount() {
		return cartAmount;
	}

	public void setCartAmount(Double cartAmount) {
		this.cartAmount = cartAmount;
	}

	@Override
	public String toString() {
		return "ClaimRequest [userId=" + userId + ", couponCode=" + couponCode + ", cartProducts=" + cartProducts
				+ ", cartAmount=" + cartAmount + "]";
	}
	
}
