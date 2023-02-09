package com.imalipay.messaging.core.configs;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@org.springframework.context.annotation.Configuration
public class ApplicationConfigs 
{
	@Value("${imalipay.messaging.out.exchange}")
	private String messagingExchange;
	
	@Value("${imalipay.messaging.out.maxAttempts}")
	private int maxSendAttempts;

	// botpress
	@Value("${imalipay.messaging.botpress.url}")
	private String botpressUrl;
	

	@Value("${imalipay.messaging.customer.otp.verify.url}")
	private String otpVerifyUrl;
}
