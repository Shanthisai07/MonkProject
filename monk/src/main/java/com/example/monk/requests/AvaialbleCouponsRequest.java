package com.example.monk.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AvaialbleCouponsRequest {
	
	private Double amount;
	
	@JsonAlias("productsInCart")
	private List<String> products;
	
	private Integer userId;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "AvaialbleCouponsRequest [amount=" + amount + ", products=" + products + ", userId=" + userId + "]";
	}
	
}
