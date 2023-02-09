package com.imalipay.messaging.bot.configs;

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

	// botpress
	@Value("${imalipay.messaging.botpress.url}")
	private String botpressUrl;
	// messaging
	@Value("${imalipay.messaging.messaging.url}")
	private String messagingUrl;
}
