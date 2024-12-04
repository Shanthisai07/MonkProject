package com.example.monk.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.monk.requests.AvaialbleCouponsRequest;
import com.example.monk.requests.AvailableCoupons;
import com.example.monk.requests.BaseResponse;
import com.example.monk.requests.ClaimCouponResponse;
import com.example.monk.requests.ClaimRequest;
import com.example.monk.requests.Coupons;
import com.example.monk.service.MonkService;

@Controller
@RequestMapping("/monk")
public class MonkController {
	
	@Autowired
	private MonkService monkService;
	
	@PostMapping({"/createOrUpdateCoupon"})
	@ResponseBody
	public BaseResponse createCoupon (@RequestBody Coupons request) {
		System.out.println("In Controller : createOrUpdateCoupon request "+ request.toString());
		BaseResponse response = monkService.createCoupon(request);
		return response;
	}
	
	@PostMapping({"/getCouponDetails"})
	@ResponseBody
	public Coupons getCouponDetails (@RequestParam Integer couponId) {
		System.out.println("In Controller : getCouponDetails.... for "+couponId);
		Coupons response = monkService.getCouponDetails(couponId);
		return response;
	}
	
	@PostMapping({"/getAllCouponDetails"})
	@ResponseBody
	public ArrayList<Coupons> getAllCouponDetails () {
		System.out.println("In Controller : getAllCouponDetails....");
		ArrayList<Coupons> response = monkService.getAllCouponDetails();
		return response;
	}
	
	@PostMapping({"/getAvailableCoupons"})
	@ResponseBody
	public HashMap<String, ArrayList<AvailableCoupons>> getAvailableCouponDetails (@RequestBody AvaialbleCouponsRequest request) {
		System.out.println("In Controller : getAvailableCouponDetails for ...."+ request.toString());
		HashMap<String, ArrayList<AvailableCoupons>> response = monkService.getAvailableCouponDetails(request);
		return response;
	}
	
	@PostMapping({"/claimCoupon"})
	@ResponseBody
	public ClaimCouponResponse claimCoupon (@RequestBody ClaimRequest request) {
		System.out.println("In Controller : claimCoupon...."+ request.toString());
		return monkService.claimCoupon(request);
	}
}
