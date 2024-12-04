package com.example.monk.DAOImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.monk.DAO.MonkDAO;
import com.example.monk.requests.AvaialbleCouponsRequest;
import com.example.monk.requests.BaseResponse;
import com.example.monk.requests.ClaimCouponResponse;
import com.example.monk.requests.ClaimRequest;
import com.example.monk.requests.Coupons;
import com.example.monk.utils.MonkUtils;

@Repository("monkDAO")
public class MonkDAOImpl implements MonkDAO {

	@Autowired
	@Qualifier("monkJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MonkUtils monkUtils;

	@Override
	public BaseResponse createCoupon(Coupons request) {
		System.out.println("In DAOImpl : createCoupon....");
		final String procedureCall = "{call proc_set_coupons_details_v1(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		Connection connection = null;
		ResultSet rs = null;
		CallableStatement cStmt = null;
		BaseResponse result = new BaseResponse();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			cStmt = connection.prepareCall(procedureCall);
			if (request.getCouponId() != null && request.getCouponId() > 0) {
				cStmt.setInt(1, request.getCouponId());
			} else {
				cStmt.setNull(1, java.sql.Types.BIGINT);
			}
			cStmt.setString(2, request.getCouponName());
			cStmt.setString(3, request.getCouponType());
			cStmt.setString(4, request.getCouponCode());
			if (request.getRequiredAmount() != null && request.getRequiredAmount() > 0) {
				cStmt.setDouble(5, request.getRequiredAmount());
			} else {
				cStmt.setNull(5, java.sql.Types.FLOAT);
			}
			if (request.getDiscountPercent() != null && request.getDiscountPercent() > 0) {
				cStmt.setDouble(6, request.getDiscountPercent());
			} else {
				cStmt.setNull(6, java.sql.Types.FLOAT);
			}
			cStmt.setInt(7, request.getThresholdLimit());
			cStmt.setString(8, monkUtils.convertListToString(request.getBuyProducts()));
			if (request.getBuyLimit() != null && request.getBuyLimit() > 0) {
				cStmt.setInt(9, request.getBuyLimit());
			} else {
				cStmt.setNull(9, java.sql.Types.BIGINT);
			}
			if (request.getGetLimit() != null && request.getGetLimit() > 0) {
				cStmt.setInt(10, request.getGetLimit());
			} else {
				cStmt.setNull(10, java.sql.Types.BIGINT);
			}
			cStmt.setString(11, monkUtils.convertListToString(request.getGetProducts()));
			// by default adding 7 days for coupon expire 7 days, 24 hrs, 60mins, 60secs
			// 1000 milliseconds//
			cStmt.setTimestamp(12,
					new Timestamp(System.currentTimeMillis()
							+ ((request.getExpiryDays() != null && request.getExpiryDays() > 0)
									? (request.getExpiryDays() * 24 * 60 * 60 * 1000)
									: 7 * 24 * 60 * 60 * 1000)));
			cStmt.setString(13,
					request.getProducts() != null ? monkUtils.convertListToString(request.getProducts()) : null);
			cStmt.setTimestamp(14, new Timestamp(System.currentTimeMillis()));
			cStmt.setString(15, request.getCreatedBy() != null ? request.getCreatedBy() : "TestUser");
			cStmt.setString(16, request.getUpdateReason());
			cStmt.registerOutParameter(17, java.sql.Types.TINYINT);
			cStmt.registerOutParameter(18, java.sql.Types.VARCHAR);
			System.out.println("cStmt.." + cStmt.toString());
			rs = cStmt.executeQuery();
			if (cStmt.getInt(17) == 1) {
				result.setStatus("SUCCESS");
			} else {
				System.out.println("Error ..." + cStmt.getString(18).toString());
				result.setStatus("FAILURE");
				result.setErrorDescription(cStmt.getString(18).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			connection.close();
			System.out.println("connection closed....");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("In DAOImpl : getCouponDetails response..." + result.toString());
		return result;
	}

	@Override
	public Coupons getCouponDetails(Integer couponId) {
		System.out.println("In DAOImpl : getCouponDetails....");
		final String procedureCall = "{call proc_get_coupons_details_by_id_v1(?)}";
		Connection connection = null;
		ResultSet rs = null;
		CallableStatement cStmt = null;
		Coupons result = new Coupons();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			cStmt = connection.prepareCall(procedureCall);
			cStmt.setInt(1, couponId);
			System.out.println("cStmt.." + cStmt.toString());
			rs = cStmt.executeQuery();
			if (rs.next()) {
				result.setCouponId(rs.getInt("c_id"));
				result.setCouponName(rs.getString("c_coupon_name"));
				result.setCouponType(rs.getString("c_coupon_type"));
				result.setCouponCode(rs.getString("c_coupon_code"));
				result.setDiscountPercent(rs.getDouble("c_discount_percent"));
				result.setThresholdLimit(rs.getInt("c_threshold_limit"));
				result.setRequiredAmount(rs.getDouble("c_required_amount"));
				result.setBuyProducts(
						rs.getString("c_buy_products") != null ? monkUtils.stringToList(rs.getString("c_buy_products"))
								: null);
				result.setGetProducts(
						rs.getString("c_get_products") != null ? monkUtils.stringToList(rs.getString("c_get_products"))
								: null);
				result.setBuyLimit(rs.getInt("c_buy_limit"));
				result.setGetLimit(rs.getInt("c_get_limit"));
				result.setExpiryDate(rs.getTimestamp("c_expiry_date"));
				result.setProducts(
						rs.getString("c_products") != null ? monkUtils.stringToList(rs.getString("c_products")) : null);
				result.setCreatedAt(rs.getTimestamp("c_created_time").getTime());
				result.setCreatedBy(rs.getString("c_created_by"));
				result.setUpdateReason(rs.getString("c_reason") != null ? rs.getString("c_reason") : null);

			}
		} catch (Exception e) {
			System.out.println("Exception in getCouponDetails.." + e);
		}
		try {
			connection.close();
			System.out.println("connection closed....");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("In DAOImpl : getCouponDetails response..." + result.toString());
		return result;
	}

	@Override
	public ArrayList<Coupons> getAllCouponDetails() {
		System.out.println("In DAOImpl : getAllCouponDetails....");
		final String procedureCall = "{call proc_get_all_coupons_details_v1}";
		Connection connection = null;
		ResultSet rs = null;
		CallableStatement cStmt = null;
		ArrayList<Coupons> allCoupons = new ArrayList<Coupons>();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			cStmt = connection.prepareCall(procedureCall);
			System.out.println("cStmt.." + cStmt.toString());
			rs = cStmt.executeQuery();
			while (rs.next()) {
				Coupons coupon = new Coupons();
				coupon.setCouponId(rs.getInt("c_id"));
				coupon.setCouponName(rs.getString("c_coupon_name"));
				coupon.setCouponType(rs.getString("c_coupon_type"));
				coupon.setCouponCode(rs.getString("c_coupon_code"));
				coupon.setDiscountPercent(rs.getDouble("c_discount_percent"));
				coupon.setThresholdLimit(rs.getInt("c_threshold_limit"));
				coupon.setRequiredAmount(rs.getDouble("c_required_amount"));
				coupon.setBuyProducts(
						rs.getString("c_buy_products") != null ? monkUtils.stringToList(rs.getString("c_buy_products"))
								: null);
				coupon.setGetProducts(
						rs.getString("c_get_products") != null ? monkUtils.stringToList(rs.getString("c_get_products"))
								: null);
				coupon.setBuyLimit(rs.getInt("c_buy_limit"));
				coupon.setGetLimit(rs.getInt("c_get_limit"));
				coupon.setExpiryDate(rs.getTimestamp("c_expiry_date"));
				coupon.setProducts(
						rs.getString("c_products") != null ? monkUtils.stringToList(rs.getString("c_products")) : null);
				coupon.setCreatedAt(rs.getTimestamp("c_created_time").getTime());
				coupon.setCreatedBy(rs.getString("c_created_by"));
				coupon.setUpdateReason(rs.getString("c_reason") != null ? rs.getString("c_reason") : null);
				allCoupons.add(coupon);
			}
		} catch (Exception e) {
			System.out.println("Exception in getAllCoupons.." + e);
		}
		try {
			connection.close();
			System.out.println("connection closed....");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("In DAOImpl : getAllCouponDetails response..." + allCoupons.toString());
		return allCoupons;
	}

	@Override
	public ArrayList<Coupons> getAvailableCoupons(AvaialbleCouponsRequest request) {
		System.out.println("In DAOImpl : getAvailableCoupons....");
		final String procedureCall = "{call proc_get_all_available_coupons_details_v1(?)}";
		Connection connection = null;
		ResultSet rs = null;
		CallableStatement cStmt = null;
		ArrayList<Coupons> availableCoupons = new ArrayList<Coupons>();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			cStmt = connection.prepareCall(procedureCall);
			cStmt.setInt(1, request.getUserId());
			System.out.println("cStmt.." + cStmt.toString());
			rs = cStmt.executeQuery();
			while (rs.next()) {
				Coupons coupon = new Coupons();
				coupon.setCouponId(rs.getInt("c_id"));
				coupon.setCouponName(rs.getString("c_coupon_name"));
				coupon.setCouponType(rs.getString("c_coupon_type"));
				coupon.setCouponCode(rs.getString("c_coupon_code"));
				coupon.setDiscountPercent(rs.getDouble("c_discount_percent"));
				coupon.setThresholdLimit(rs.getInt("c_threshold_limit"));
				coupon.setRequiredAmount(rs.getDouble("c_required_amount"));
				coupon.setBuyProducts(
						rs.getString("c_buy_products") != null ? monkUtils.stringToList(rs.getString("c_buy_products"))
								: null);
				coupon.setGetProducts(
						rs.getString("c_get_products") != null ? monkUtils.stringToList(rs.getString("c_get_products"))
								: null);
				coupon.setBuyLimit(rs.getInt("c_buy_limit"));
				coupon.setGetLimit(rs.getInt("c_get_limit"));
				coupon.setExpiryDate(rs.getTimestamp("c_expiry_date"));
				coupon.setProducts(
						rs.getString("c_products") != null ? monkUtils.stringToList(rs.getString("c_products")) : null);
				coupon.setCreatedAt(rs.getTimestamp("c_created_time").getTime());
				coupon.setCreatedBy(rs.getString("c_created_by"));
				coupon.setUpdateReason(rs.getString("c_reason") != null ? rs.getString("c_reason") : null);
				availableCoupons.add(coupon);
			}
		} catch (Exception e) {
			System.out.println("Exception in getAvailableCoupons.." + e);
		}
		try {
			connection.close();
			System.out.println("connection closed....");
		} catch (SQLException e) {
			System.out.println("Exception in getAvailableCoupons while connection closing.." + e);
		}
		System.out.println("In DAOImpl : getAvailableCoupons response..." + availableCoupons.toString());
		return availableCoupons;
	}

	@Override
	public ClaimCouponResponse claimCoupon(ClaimRequest request) {
		System.out.println("In DAOImpl : claimCoupon....");
		final String procedureCall = "{call proc_apply_coupon_v1(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
		Connection connection = null;
		CallableStatement cStmt = null;
		ClaimCouponResponse response = new ClaimCouponResponse();
		try {
			connection = jdbcTemplate.getDataSource().getConnection();
			cStmt = connection.prepareCall(procedureCall);
			cStmt.setInt(1, request.getUserId());
			cStmt.setDouble(2, request.getCartAmount());
			cStmt.setString(3, monkUtils.convertListToString(new ArrayList<>(request.getCartProducts().keySet())));
			cStmt.setString(4, monkUtils.convertListToString(new ArrayList<>(request.getCartProducts().values())));
			cStmt.setString(5, request.getCouponCode());
			cStmt.registerOutParameter(6, java.sql.Types.DOUBLE);
			cStmt.registerOutParameter(7, java.sql.Types.INTEGER);
			cStmt.registerOutParameter(8, java.sql.Types.INTEGER);
			cStmt.registerOutParameter(9, java.sql.Types.VARCHAR);
			System.out.println("cStmt.." + cStmt.toString());
			cStmt.executeQuery();
			if (cStmt.getInt("out_status") == 0) {
				response.setStatus("FAILURE");
				response.setErrorDescription(cStmt.getString("out_error_msg"));
			} else if (cStmt.getInt("out_status") == 1) {
				response.setStatus("SUCCESS");
				response.setAmountAfterCouponApplied(cStmt.getDouble("out_amount"));
				response.setThresholdLimit(cStmt.getInt("out_claim_limit"));
			} else if (cStmt.getInt("out_status") == MonkUtils.Common_Error_Code.INVALID_COUPON_CODE.getErrorId()) {
				response.setStatus("FAILURE");
				response.setErrorCode(MonkUtils.Common_Error_Code.INVALID_COUPON_CODE.toString());
				response.setErrorDescription(cStmt.getString("out_error_msg"));
				response.setAmountAfterCouponApplied(cStmt.getDouble("out_amount"));
				response.setThresholdLimit(cStmt.getInt("out_claim_limit"));
			} else if (cStmt.getInt("out_status") == MonkUtils.Common_Error_Code.COUPON_EXPIRED.getErrorId()) {
				response.setStatus("FAILURE");
				response.setErrorCode(MonkUtils.Common_Error_Code.COUPON_EXPIRED.toString());
				response.setErrorDescription(cStmt.getString("out_error_msg"));
				response.setAmountAfterCouponApplied(cStmt.getDouble("out_amount"));
				response.setThresholdLimit(cStmt.getInt("out_claim_limit"));
			} else if (cStmt.getInt("out_status") == MonkUtils.Common_Error_Code.THRESHOLD_LIMIT_EXCEEDED
					.getErrorId()) {
				response.setStatus("FAILURE");
				response.setErrorCode(MonkUtils.Common_Error_Code.THRESHOLD_LIMIT_EXCEEDED.toString());
				response.setErrorDescription(cStmt.getString("out_error_msg"));
				response.setAmountAfterCouponApplied(cStmt.getDouble("out_amount"));
				response.setThresholdLimit(cStmt.getInt("out_claim_limit"));
			} else if (cStmt.getInt("out_status") == MonkUtils.Common_Error_Code.INSUFFICIENT_CART_AMOUNT
					.getErrorId()) {
				response.setStatus("FAILURE");
				response.setErrorCode(MonkUtils.Common_Error_Code.INSUFFICIENT_CART_AMOUNT.toString());
				response.setErrorDescription(cStmt.getString("out_error_msg"));
				response.setAmountAfterCouponApplied(cStmt.getDouble("out_amount"));
				response.setThresholdLimit(cStmt.getInt("out_claim_limit"));
			} else if (cStmt.getInt("out_status") == MonkUtils.Common_Error_Code.INSUFFICIENT_PRODUCTS.getErrorId()) {
				response.setStatus("FAILURE");
				response.setErrorCode(MonkUtils.Common_Error_Code.INSUFFICIENT_PRODUCTS.toString());
				response.setErrorDescription(cStmt.getString("out_error_msg"));
				response.setAmountAfterCouponApplied(cStmt.getDouble("out_amount"));
				response.setThresholdLimit(cStmt.getInt("out_claim_limit"));
			}

		} catch (Exception e) {
			System.out.println("Exception in claimCoupon.." + e);
		}
		try {
			connection.close();
			System.out.println("connection closed....");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("In DAOImpl : claimCoupon response..." + response);

		return response;
	}

}
