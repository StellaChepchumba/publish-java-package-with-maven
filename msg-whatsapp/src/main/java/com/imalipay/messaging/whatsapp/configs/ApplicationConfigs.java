package com.imalipay.messaging.whatsapp.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class ApplicationConfigs 
{
	// rest template 
	@Value("${restTemplate.connection.connect-timeout}")
	private Integer connectionTimeout;
	@Value("${restTemplate.connection.read-timeout}")
	private Integer readTimeout;
	@Value("${restTemplate.connection.request-timeout}")
	private Integer requestTimeout;
	@Value("${restTemplate.connection-pool.max-connections}")
	private Integer maxConnections;
	@Value("${restTemplate.connection-pool.max-connections-per-route}")
	private Integer maxConnectionsPerRoute;
	
	// gupshup
	@Value("${gupshup.api.key}")
	private String gupshupApiKey;
	@Value("${gupshup.api.url}")
	private String gupshupUrl;
	
	// rabbitmq
	@Value("${imalipay.messaging.out.exchange}")
	private String messagingExchange;
	@Value("${imalipay.messaging.queue.dlr}")
	private String queueDeliveryReport;
}
