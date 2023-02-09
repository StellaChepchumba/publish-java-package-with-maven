package com.imalipay.messaging.sms.configs;

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
	
	// ait 
	@Value("${imalipay.messaging.sms.config.http.ait.host}")
	private String aitHost;
	@Value("${imalipay.messaging.sms.config.http.ait.api-key}")
	private String aitApiKey;
	@Value("${imalipay.messaging.sms.config.http.ait.username}")
	private String aitUsername;

	// infobip nigeria 
	@Value("${imalipay.messaging.sms.config.infobip.nigeria.key.prefix}")
	private String apiKeyPrefix;
	@Value("${imalipay.messaging.sms.config.infobip.nigeria.baseUrl}")
	private String baseUrl;
	@Value("${imalipay.messaging.sms.config.infobip.nigeria.key}")
	private String key;

	// infobip south africa 
	@Value("${imalipay.messaging.sms.config.infobip.south-africa.key.prefix}")
	private String saApiKeyPrefix;
	@Value("${imalipay.messaging.sms.config.infobip.south-africa.baseUrl}")
	private String saBaseUrl;
	@Value("${imalipay.messaging.sms.config.infobip.south-africa.key}")
	private String saKey;
}
