package com.example.monk.requests;

public class BaseResponse {
	
	private String status;
	
	private String errorDescription;
	
	private String errorCode;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "BaseResponse [status=" + status + ", errorDescription=" + errorDescription + ", errorCode=" + errorCode
				+ "]";
	}

}
