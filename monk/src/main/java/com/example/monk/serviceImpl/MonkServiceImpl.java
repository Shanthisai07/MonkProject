package com.example.monk.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.monk.DAO.MonkDAO;
import com.example.monk.requests.AvaialbleCouponsRequest;
import com.example.monk.requests.AvailableCoupons;
import com.example.monk.requests.BaseResponse;
import com.example.monk.requests.ClaimCouponResponse;
import com.example.monk.requests.ClaimRequest;
import com.example.monk.requests.Coupons;
import com.example.monk.service.MonkService;
import com.example.monk.utils.MonkUtils;
import com.example.monk.utils.MonkUtils.Common_Error_Code;

@Service
public class MonkServiceImpl implements MonkService {

	@Autowired
	@Qualifier("monkDAO")
	private MonkDAO monkDAO;

	@Autowired
	private MonkUtils monkUtils;

	@Override
	public BaseResponse createCoupon(Coupons request) {
		System.out.println("In Service Impl : createCoupon....");
		BaseResponse response = new BaseResponse();
		response = monkUtils.validateCreation(request);
		if (response.getStatus() == null) {
			return monkDAO.createCoupon(request);
		}
		return response;
	}

	@Override
	public Coupons getCouponDetails(Integer couponId) {
		System.out.println("In Service Impl : getCouponDetails....");
		return monkDAO.getCouponDetails(couponId);
	}

	@Override
	public ArrayList<Coupons> getAllCouponDetails() {
		System.out.println("In Service Impl : getAllCouponDetails....");
		return monkDAO.getAllCouponDetails();
	}

	@Override
	public HashMap<String, ArrayList<AvailableCoupons>> getAvailableCouponDetails(AvaialbleCouponsRequest request) {
		System.out.println("In Service Impl : getAvailableCouponDetails....");
		ArrayList<Coupons> availableCoupons = monkDAO.getAvailableCoupons(request);
		HashMap<String, ArrayList<AvailableCoupons>> allAvailableCoupons = new HashMap<String, ArrayList<AvailableCoupons>>();
		ArrayList<AvailableCoupons> availableCouponsToClaim = new ArrayList<AvailableCoupons>();
		ArrayList<AvailableCoupons> couponsCanBeClaimed = new ArrayList<AvailableCoupons>();
		for (int i = 0; i < availableCoupons.size(); i++) {
			AvailableCoupons coupon = new AvailableCoupons();
			coupon.setCouponCode(availableCoupons.get(i).getCouponCode());
			coupon.setCouponId(availableCoupons.get(i).getCouponId());
			coupon.setCouponName(availableCoupons.get(i).getCouponName());
			coupon.setCouponType(availableCoupons.get(i).getCouponType());
			coupon.setExpiryDate(availableCoupons.get(i).getExpiryDate());
			coupon.setThresholdLimit(availableCoupons.get(i).getThresholdLimit());
//			System.out.println("CouponName : "+ availableCoupons.get(i).getCouponName());
			if (availableCoupons.get(i).getCouponType()
					.equalsIgnoreCase(MonkUtils.couponType.Product_wise.toString())) {
				if (request.getProducts().size() > 0) {
					for (int j = 0; j < request.getProducts().size(); j++) {
						if (availableCoupons.get(i).getProducts().contains(request.getProducts().get(j))) {
							coupon.setApplicable(true);
							availableCouponsToClaim.add(coupon);
							continue;
						}
					}
				} else {
					coupon.setApplicable(false);
					coupon.setRequiredCondition("Please add any of these Products "
							+ availableCoupons.get(i).getProducts().toString() + " to claim.");
					couponsCanBeClaimed.add(coupon);
					continue;
				}
			} else if (availableCoupons.get(i).getCouponType()
					.equalsIgnoreCase(MonkUtils.couponType.Cart_wise.toString())) {
				if (request.getAmount() > 0 && (availableCoupons.get(i).getRequiredAmount() <= request.getAmount())) {
					coupon.setApplicable(true);
					availableCouponsToClaim.add(coupon);
					continue;
				} else {
					coupon.setApplicable(false);
					coupon.setRequiredCondition("Cart Total Amount Should reach "
							+ availableCoupons.get(i).getRequiredAmount().toString() + " to claim.");
					couponsCanBeClaimed.add(coupon);
					continue;
				}
			} else if (availableCoupons.get(i).getCouponType().equalsIgnoreCase(MonkUtils.couponType.BxGy.toString())) {
				Integer buyProductsCount = 0;
				Integer getProductsCount = 0;
				if (request.getProducts().size() > 0) {
					for (int j = 0; j < request.getProducts().size(); j++) {
						if (availableCoupons.get(i).getBuyProducts().contains(request.getProducts().get(j))) {
							buyProductsCount += 1;
						} else if (availableCoupons.get(i).getGetProducts().contains(request.getProducts().get(j))) {
							getProductsCount += 1;
						}
					}
				}
				System.out.println("buyProductsCount : "+buyProductsCount+" , "+"getProductsCount :"+getProductsCount);
				if (buyProductsCount >= 0) {
					if (buyProductsCount >= availableCoupons.get(i).getBuyLimit()) {
						if (getProductsCount > 0) {
							if (getProductsCount >= availableCoupons.get(i).getGetLimit()) {
								coupon.setApplicable(true);
								availableCouponsToClaim.add(coupon);
								continue;
							} else {
								coupon.setApplicable(false);
								coupon.setRequiredCondition(
										"Please add " + (availableCoupons.get(i).getGetLimit() - getProductsCount)
												+ " more products from "
												+ availableCoupons.get(i).getGetProducts().toString() + " to claim.");
								couponsCanBeClaimed.add(coupon);
								continue;
							}
						} else {
							coupon.setApplicable(false);
							coupon.setRequiredCondition(
									"Please add " + (availableCoupons.get(i).getGetLimit()) + " more products from "
											+ availableCoupons.get(i).getGetProducts().toString() + " to claim.");
							couponsCanBeClaimed.add(coupon);
							continue;
						}
					} else if (buyProductsCount < availableCoupons.get(i).getBuyLimit() && getProductsCount > 0
							&& getProductsCount >= availableCoupons.get(i).getGetLimit()) {
						coupon.setApplicable(false);
						coupon.setRequiredCondition("Please add "
								+ (availableCoupons.get(i).getBuyLimit() - buyProductsCount) + " more products from "
								+ availableCoupons.get(i).getBuyProducts().toString() + " to claim.");
						couponsCanBeClaimed.add(coupon);
						continue;
					} else if (buyProductsCount < availableCoupons.get(i).getBuyLimit() && getProductsCount > 0
							&& getProductsCount >= availableCoupons.get(i).getGetLimit()) {
						coupon.setApplicable(false);
						coupon.setRequiredCondition("Please add "
								+ (availableCoupons.get(i).getBuyLimit() - buyProductsCount) + " more products from "
								+ availableCoupons.get(i).getBuyProducts().toString() + " and "
								+ (availableCoupons.get(i).getGetLimit()) + " more products from "
								+ availableCoupons.get(i).getGetProducts().toString() + " to claim.");
						couponsCanBeClaimed.add(coupon);
						continue;
					}
				}
			}
		}
		allAvailableCoupons.put("availableCouponsToClaim", availableCouponsToClaim);
		allAvailableCoupons.put("couponsCanBeClaimed", couponsCanBeClaimed);
		System.out.println("In Service Impl : getAvailableCouponDetails ...." + allAvailableCoupons.toString());
		return allAvailableCoupons;
	}

	@Override
	public ClaimCouponResponse claimCoupon(ClaimRequest request) {
		System.out.println("In Service Impl : claimCoupon...."+ request.toString());
		ClaimCouponResponse response = new ClaimCouponResponse();
		response = monkUtils.validateClaimRequest(request);
		if(response.getStatus() == null) {
			response = monkDAO.claimCoupon(request);
		}
		return response;
	}

}
