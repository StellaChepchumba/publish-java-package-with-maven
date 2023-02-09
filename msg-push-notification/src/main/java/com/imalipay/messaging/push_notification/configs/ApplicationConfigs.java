package com.imalipay.messaging.push_notification.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class ApplicationConfigs 
{
	// rabbitmq
	@Value("${imalipay.messaging.out.exchange}")
	private String messagingExchange;
	@Value("${imalipay.messaging.queue.dlr}")
	private String queueDeliveryReport;
	
	// firebase 
	@Value("${imalipay.messaging.push_notification.firebase.app_name}")
	private String firebaseAppName;
	@Value("${imalipay.messaging.push_notification.firebase.cred_path}")
	private String credentialPath;
}
