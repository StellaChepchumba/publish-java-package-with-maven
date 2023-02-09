package com.imalipay.messaging.library.dtos;

public class StatusCodes 
{
	private StatusCodes() {}
	
	public static final String SUCCESS = "0000";
	public static final String FAILED = "9000";
	public static final String NOT_FOUND = "9001";
	public static final String OTP_EXPIRED = "9002";
	public static final String OTP_EXHAUSTED_ATTEMPTS = "9003";
	public static final String OTP_INCORRECT = "9004";
	public static final String CUSTOMER_FOUND_DEVICE_NOT_FOUND = "9005";
	public static final String CUSTOMER_FOUND_DEVICE_FOUND = "9006";
}
