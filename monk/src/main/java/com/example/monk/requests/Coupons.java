package com.example.monk.requests;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class Coupons {
	
	private Integer couponId;
	
	private String couponName;
	
	private String couponType;
	
	private String couponCode;
	
	private Double requiredAmount;
	
	private Double discountPercent;
	
	private Integer thresholdLimit;
	
	private ArrayList<String> buyProducts;
	
	private ArrayList<String> getProducts;
	
	private Integer buyLimit;
	
	private Integer getLimit;
	
	private String createdBy;
	
	private String updateReason;
	
	private ArrayList<String> products;
	
	private Long expiryDays;
	
	private Long createdAt;
	
	private Timestamp expiryDate;

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Double getRequiredAmount() {
		return requiredAmount;
	}

	public void setRequiredAmount(Double requiredAmount) {
		this.requiredAmount = requiredAmount;
	}

	public Double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public Integer getThresholdLimit() {
		return thresholdLimit;
	}

	public void setThresholdLimit(Integer thresholdLimit) {
		this.thresholdLimit = thresholdLimit;
	}

	public ArrayList<String> getBuyProducts() {
		return buyProducts;
	}

	public void setBuyProducts(ArrayList<String> buyProducts) {
		this.buyProducts = buyProducts;
	}

	public ArrayList<String> getGetProducts() {
		return getProducts;
	}

	public void setGetProducts(ArrayList<String> getProducts) {
		this.getProducts = getProducts;
	}

	public Integer getBuyLimit() {
		return buyLimit;
	}

	public void setBuyLimit(Integer buyLimit) {
		this.buyLimit = buyLimit;
	}

	public Integer getGetLimit() {
		return getLimit;
	}

	public void setGetLimit(Integer getLimit) {
		this.getLimit = getLimit;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdateReason() {
		return updateReason;
	}

	public void setUpdateReason(String updateReason) {
		this.updateReason = updateReason;
	}

	public ArrayList<String> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<String> products) {
		this.products = products;
	}

	public Long getExpiryDays() {
		return expiryDays;
	}

	public void setExpiryDays(Long expiryDays) {
		this.expiryDays = expiryDays;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Timestamp expiryDate) {
		this.expiryDate = expiryDate;
	}

	@Override
	public String toString() {
		return "Coupons [couponId=" + couponId + ", couponName=" + couponName + ", couponType=" + couponType
				+ ", couponCode=" + couponCode + ", requiredAmount=" + requiredAmount + ", discountPercent="
				+ discountPercent + ", thresholdLimit=" + thresholdLimit + ", buyProducts=" + buyProducts
				+ ", getProducts=" + getProducts + ", buyLimit=" + buyLimit + ", getLimit=" + getLimit + ", createdBy="
				+ createdBy + ", updateReason=" + updateReason + ", products=" + products + ", expiryDays=" + expiryDays
				+ ", createdAt=" + createdAt + ", expiryDate=" + expiryDate + "]";
	}
	
}
