package com.example.monk.DAO;

import java.util.ArrayList;

import com.example.monk.requests.AvaialbleCouponsRequest;
import com.example.monk.requests.BaseResponse;
import com.example.monk.requests.ClaimCouponResponse;
import com.example.monk.requests.ClaimRequest;
import com.example.monk.requests.Coupons;

public interface MonkDAO {
	
	public BaseResponse createCoupon(Coupons request);
	
	public Coupons getCouponDetails(Integer couponId);

	public ArrayList<Coupons> getAllCouponDetails();

	public ArrayList<Coupons> getAvailableCoupons(AvaialbleCouponsRequest request);
	
	public ClaimCouponResponse claimCoupon(ClaimRequest request);

}
