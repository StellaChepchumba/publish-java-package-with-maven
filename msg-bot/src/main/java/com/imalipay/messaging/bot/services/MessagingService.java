package com.imalipay.messaging.bot.services;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.imalipay.messaging.bot.configs.ApplicationConfigs;
import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.imalipay.messaging.library.dtos.SendMessageDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class MessagingService 
{
	private final ApplicationConfigs configs;
	private final RestTemplate restTemplate = new RestTemplate();
	
	
	
	public void send(
		String to, 
		String from,
		String body,
		Map<String, String> data, 
		String countryCode,
		DeliveryMode deliveryMode
	)
	{
		log.debug("sending out {}", deliveryMode);
		var messageRequest = SendMessageDTO.builder()
				.to(to)
				.from(from)
				.body(body)
				.extraData(data)
				.build();
		
		log.trace("messaging being sent out: {}", messageRequest);
		
		var response = restTemplate.postForObject(
				configs.getMessagingUrl(), 
				messageRequest, 
				String.class,
				countryCode, 
				deliveryMode);
		
		log.debug("send response: {}", response);
	}
}
