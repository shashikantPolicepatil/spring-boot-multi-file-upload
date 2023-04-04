package com.exception.demo.dto;

public class ResponseData {
	
	String code;
	String message;
	
	public ResponseData(String code,String message) {
this.code=code;this.message=message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	

}
