# MonkCouponsProject

## Overview
The **MonkCouponsProject** is a Spring Boot application designed to manage and apply promotional coupons efficiently in e-commerce systems. It offers RESTful APIs for creating, updating, and applying coupons, with robust validation logic and user history tracking.  

---

## Features
- **Create & Manage Coupons**: Easily define coupon types, discounts, and usage thresholds.
- **Check Availability**: Determine eligible coupons based on cart details.
- **Apply Coupons**: Automatically calculate discounts and enforce coupon rules.
- **User Tracking**: Maintain detailed logs of coupon usage.

---

## Quick Start Guide

### 1. Prerequisites
- **Java 17+**
- **Maven 3+**
- **MySQL Database**
- Docker (Optional, for testing with Testcontainers)

---

### 2. Setup

#### Clone the Repository
  bash
  git clone <repository_url>
  cd monk

#### Configure the Database
  Update application.properties with your MySQL credentials:
  
    spring.datasource.url=jdbc:mysql://localhost:3306/monk
    spring.datasource.username=your_username
    spring.datasource.password=your_password

### Initialize the Database
  Run the SQL scripts in the procedures folder:
  
    TableSchema.sql to set up tables.
    proc_apply_coupon_v1.sql and other stored procedures.


### 3. Build and Run
    Using Maven
    bash
    ./mvnw clean install
    ./mvnw spring-boot:run
### 4. Access the Application
  API endpoints are available at:

    http://localhost:8080/monk
  
**API Reference**
  
**1. Create or Update Coupon**

Endpoint: /monk/createOrUpdateCoupon

Method: POST

Request Example:
  json
    {
      "couponCode": "SUMMER20",
      "couponName": "Summer Sale",
      "couponType": "Cart_wise",
      "discountPercent": 20,
      "requiredAmount": 2000,
      "thresholdLimit": 5,
      "expiryDays": 30
    }
  
Response:
  json
  {
    "status": "SUCCESS",
    "errorDescription": null
  }
  
**2. Apply Coupon**

Endpoint: /monk/claimCoupon

Method: POST

Request Example:
  
json
  {
    "userId": 1234,
    "cartAmount": 3000,
    "couponCode": "SUMMER20",
    "cartProducts": ["P1", "P2"],
    "cartEachProductPrice": {"P1": 1500, "P2": 1500}
  }
  
Response (Success):
  json
  {
    "status": "SUCCESS",
    "amountAfterCouponApplied": 2400,
    "claimLimit": 4
  }
  
Response (Failure):
  json
  {
    "status": "FAILURE",
    "errorDescription": "Cart total amount must exceed 2000"
  }

### Business Logic

  Coupon Types

    Cart-wise:

      Discount applies if the total cart amount exceeds a threshold.

      Example: SUMMER20 provides a 20% discount on carts above $2000.

    Product-wise:

      Discounts apply only to specific products in the cart.

    Buy X Get Y (BxGy):

      Buy specific products to get discounts on others.

**Error Handling**

  Some of the Custom Status Codes:

    1: Success.
    4: Invalid coupon code.
    8: Coupon expired.
    9: Claim limit exceeded.

**Enhancements: Optimized Proposal**:

1. Use Redis for Caching

  Leveraging Redis can significantly improve performance by reducing the number of direct database queries, especially for frequently accessed data like coupons and user histories.
  
  Optimized Workflow:
  
    1. Coupon Data:
  
      Hash Key: Use COUPON:<couponCode> as the Redis hash key.
      Insertion: On inserting or updating a coupon in the database, push the updated data to Redis.
      Fetch Logic:
        Attempt to fetch coupon details from Redis.
        If not found, retrieve it from the database, then store it in Redis for subsequent requests.

    2. User History:
  
      Hash Key: Use USER:<userId>:COUPONS for tracking coupon claims for a specific user.
      Insertion: Update Redis when a user claims a coupon or their claim history changes.
      Fetch Logic:
        First, attempt to fetch from Redis.
        Fallback to the database if no data is found, and then populate Redis.

2. Implement CLAIM_IN_PROGRESS

  Handling concurrent claims efficiently requires a mechanism to track in-progress claims and prevent duplicate or conflicting operations.
  
  Optimized Workflow:
  
    1.Generate Unique Claim ID:
  
      Use a combination of couponCode and userId (e.g., CLAIM:<couponCode>:<userId>) as the unique claim ID.
      
    2.Store Pending Claims:
  
      Add a new table (tbl_pending_claims) to track claims that are currently being processed:
      
      During a claim request:
      Check if the claim ID exists in tbl_pending_claims.
      If present, return a CLAIM_IN_PROGRESS response.
      Otherwise, insert the claim into the table and proceed.

    3.Cleanup on Success or Failure:
  
      Once a claim is successfully processed or fails, remove the corresponding entry from tbl_pending_claims.

**Benefits**:

  1.Performance:
      
    Faster reads with Redis.
    Reduced database contention with caching and pending claim handling.
    
  2.Consistency:

    Prevents race conditions for coupon claims.
    Ensures consistency when multiple requests are made for the same coupon by the same user.
   
