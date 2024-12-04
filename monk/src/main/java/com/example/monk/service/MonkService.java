package com.example.monk.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.monk.requests.AvaialbleCouponsRequest;
import com.example.monk.requests.AvailableCoupons;
import com.example.monk.requests.BaseResponse;
import com.example.monk.requests.ClaimCouponResponse;
import com.example.monk.requests.ClaimRequest;
import com.example.monk.requests.Coupons;

public interface MonkService {
	
	public BaseResponse createCoupon(Coupons request);

	public Coupons getCouponDetails(Integer couponId);

	public ArrayList<Coupons> getAllCouponDetails();

	public HashMap<String, ArrayList<AvailableCoupons>> getAvailableCouponDetails(AvaialbleCouponsRequest request);
	
	public ClaimCouponResponse claimCoupon(ClaimRequest request);
}
