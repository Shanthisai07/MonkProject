package com.example.monk.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.monk.requests.BaseResponse;
import com.example.monk.requests.ClaimCouponResponse;
import com.example.monk.requests.ClaimRequest;
import com.example.monk.requests.Coupons;
import com.example.monk.requests.ErrorCode;
import io.micrometer.common.util.StringUtils;

@Component
public class MonkUtils {

	public static enum couponType {
		Cart_wise, Product_wise, BxGy
	};

	public static final String listSeperator = ",";

	public static enum Common_Error_Code implements ErrorCode {

		INVALID_DATA_FOR_CART_WISE(1, "Please enter only Required Amount and Discount Percent."),

		INVALID_DATA_FOR_PRODUCT_WISE(2, "Please enter only Products and Discount Percent."),

		INVALID_DATA_FOR_BxGy(3, "Please enter only Buy/Get Products and Buy/Get Limits."),

		INVALID_COUPON_CODE(4, "Please enter Coupon code."),

		INVALID_COUPON_NAME(5, "Please enter Coupon name."),

		INVALID_THRESHOLD_LIMIT(6, "Please enter Threshold Limit."),

		INVALID_COUPON_TYPE(7, "Please enter Coupon Type."),
		
		COUPON_EXPIRED(8, "Coupon got expired."),
		
		THRESHOLD_LIMIT_EXCEEDED(9, "Coupon claim limit exceeded."),
		
		INSUFFICIENT_CART_AMOUNT(10, "Cart Amount limit not reached."),
		
		INSUFFICIENT_PRODUCTS(11, "No Eligible Products in cart.");

		private String errorDescription;

		private Integer errorId;

		Common_Error_Code(int id, String description) {
			this.errorId = id;
			this.errorDescription = description;
		}

		@Override
		
		public String getErrorDescription() {
			return errorDescription;
		}

		@Override
		public Integer getErrorId() {
			return errorId;
		}

	}

	public BaseResponse validateCreation(Coupons request) {
		BaseResponse response = new BaseResponse();
		if (!StringUtils.isNotBlank(request.getCouponCode())) {
			response.setErrorCode(Common_Error_Code.INVALID_COUPON_CODE.toString());
			response.setStatus("FAILURE");
			response.setErrorDescription(Common_Error_Code.INVALID_COUPON_CODE.getErrorDescription());
			return response;
		}

		if (!StringUtils.isNotBlank(request.getCouponName())) {
			response.setErrorCode(Common_Error_Code.INVALID_COUPON_NAME.toString());
			response.setStatus("FAILURE");
			response.setErrorDescription(Common_Error_Code.INVALID_COUPON_NAME.getErrorDescription());
			return response;
		}

		if (request.getThresholdLimit() == null || request.getThresholdLimit() <= 0) {
			response.setErrorCode(Common_Error_Code.INVALID_THRESHOLD_LIMIT.toString());
			response.setStatus("FAILURE");
			response.setErrorDescription(Common_Error_Code.INVALID_THRESHOLD_LIMIT.getErrorDescription());
			return response;
		}

		if (!StringUtils.isNotBlank(request.getCouponType())) {
			response.setErrorCode(Common_Error_Code.INVALID_COUPON_TYPE.toString());
			response.setStatus("FAILURE");
			response.setErrorDescription(Common_Error_Code.INVALID_COUPON_TYPE.getErrorDescription());
			return response;
		}

		if (request.getCouponType().equalsIgnoreCase(couponType.Cart_wise.toString())) {
			if ((request.getRequiredAmount() == null || request.getRequiredAmount() <= 0)
					|| (request.getDiscountPercent() == null || request.getDiscountPercent() <= 0)
					|| request.getBuyLimit() != null || request.getBuyProducts() != null
					|| request.getGetLimit() != null || request.getGetProducts() != null
					|| request.getProducts() != null) {
				response.setErrorCode(Common_Error_Code.INVALID_DATA_FOR_CART_WISE.toString());
				response.setStatus("FAILURE");
				response.setErrorDescription(Common_Error_Code.INVALID_DATA_FOR_CART_WISE.getErrorDescription());
				return response;
			}
		}

		if (request.getCouponType().equalsIgnoreCase(couponType.Product_wise.toString())) {
			if ((request.getProducts() == null || request.getProducts().size() == 0)
					|| (request.getDiscountPercent() == null || request.getDiscountPercent() <= 0)
					|| (request.getBuyLimit() != null)
					|| (request.getBuyProducts() != null)
					|| (request.getGetLimit() != null )
					|| (request.getGetProducts() != null)
					|| (request.getRequiredAmount() != null)) {
				response.setErrorCode(Common_Error_Code.INVALID_DATA_FOR_PRODUCT_WISE.toString());
				response.setStatus("FAILURE");
				response.setErrorDescription(Common_Error_Code.INVALID_DATA_FOR_PRODUCT_WISE.getErrorDescription());
				return response;
			}
		}

		if (request.getCouponType().equalsIgnoreCase(couponType.BxGy.toString())) {
			if ((request.getBuyLimit() == null || request.getBuyLimit() <= 0)
					|| (request.getBuyProducts() == null || request.getBuyProducts().size() == 0)
					|| (request.getGetLimit() == null || request.getGetLimit() <= 0)
					|| (request.getGetProducts() == null || request.getGetProducts().size() == 0)
					|| (request.getProducts() != null)
					|| (request.getDiscountPercent() != null)
					|| (request.getRequiredAmount() != null)) {
				response.setErrorCode(Common_Error_Code.INVALID_DATA_FOR_BxGy.toString());
				response.setStatus("FAILURE");
				response.setErrorDescription(Common_Error_Code.INVALID_DATA_FOR_BxGy.getErrorDescription());
				return response;
			}
		}

		return response;
	}

	public <T> String convertListToString(List<T> listStr) {
		return getDemarkatedString(listStr, listSeperator);
	}

	public static <T> String getDemarkatedString(List<T> listStr, String demarkator) {
		String csv = null;
		if (listStr != null && listStr.size() >= 0) {
			csv = listStr.toString().replace("[", "").replace("]", "").replace(", ", demarkator);
		}
		return csv;
	}

	public String hashMapToSqlString(Map<String, ArrayList<String>> map) {
		StringBuffer buffer = new StringBuffer();
		int length = map.size();
		if (map != null) {
			for (String couponId : map.keySet()) {
				buffer.append(couponId).append("|");
				buffer.append(map.get(couponId));
				if (length > 1)
					buffer.append("~");
				length -= 1;
			}
		}
		return buffer.toString();
	}

	public HashMap<String, ArrayList<String>> stringToMap(String data) {
		System.out.println("In stringToMap ... " + data);
		HashMap<String, ArrayList<String>> result = new HashMap<String, ArrayList<String>>();
		if (data != null) {
			String[] keySets = data.split("~");
			for (int i = 0; i < keySets.length; i++) {
				ArrayList<String> productList = new ArrayList<String>();
				String[] eachKeyValue = keySets[i].split("\\|");
				if (eachKeyValue.length == 2) {
					System.out.println(String.valueOf(eachKeyValue[0]));
					String[] products = eachKeyValue[1].replace("[", "").replace("]", "").split(",");
					System.out.println(Arrays.toString(products));
					for (int j = 0; j < products.length; j++)
						productList.add(products[j].trim());
					result.put(String.valueOf(eachKeyValue[0]), productList);
				}
			}
		}
		System.out.println("In stringToMap after conversion... " + result.toString());
		return result;
	}

	public ArrayList<String> stringToList(String inList) {
		ArrayList<String> requiredList = new ArrayList<String>(Arrays.asList(inList.split(listSeperator)));
		return requiredList;
	}
	
	public ClaimCouponResponse validateClaimRequest(ClaimRequest request) {
		ClaimCouponResponse response = new ClaimCouponResponse();
		if (request.getUserId() == null || request.getUserId() < 1) {
			response.setStatus("FAILURE");
			response.setErrorDescription("Please provide UserId.");
			return response;
		} else if (!StringUtils.isNotBlank(request.getCouponCode())) {
			response.setStatus("FAILURE");
			response.setErrorDescription("Please provide coupon code.");
			return response;
		} else if (request.getCartAmount() == null || request.getCartAmount() <= 0) {
			response.setStatus("FAILURE");
			response.setErrorDescription("Please provide Cart Amount.");
			return response;
		} else if (request.getCartProducts() == null || request.getCartProducts().size() == 0) {
			response.setStatus("FAILURE");
			response.setErrorDescription("Please provide Cart Products.");
			return response;
		} else if (request.getCartProducts() != null) {
			for (Double value : request.getCartProducts().values()) {
				if (value <= 0.0) {
					response.setStatus("FAILURE");
					response.setErrorDescription("Please provide valid products amount.");
					return response;
				}
			}
		} else if (request.getCartAmount() != sum(request.getCartProducts())) {
			response.setStatus("FAILURE");
			response.setErrorDescription("Cart amount mismatch with sum of products amount.");
			return response;
		}
		return response;
	}
	
	public Double sum(HashMap<String, Double> products) {
        Double sum = 0.0;
        for (Double num : products.values()) {
            sum += num;
        }
        return sum;
    }
}
