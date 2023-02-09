package com.imalipay.messaging.whatsapp.listener;

import java.time.ZonedDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.whatsapp.configs.ApplicationConfigs;
import com.imalipay.messaging.whatsapp.configs.gupshup.GupshupStatusMapper;
import com.imalipay.messaging.whatsapp.services.GupshupService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class GupshupListener 
{
	private final ApplicationConfigs configs;
	private final RabbitTemplate rabbitTemplate;
	private final GupshupService gupshupService;
	
	@RabbitListener(queues = "${imalipay.messaging.out.whatsapp.gupshup}")
	public void recievedMessage(MessageDTO message) throws Exception 
	{
		log.trace("recieved message from rabbitMQ: {}", message);
		
		var response = gupshupService.sendMessage(message);

		// update message 
		message.setExternalId(response.getMessageId());
		message.setStatus(GupshupStatusMapper.map(response.getStatus()));
		message.setSentAt(ZonedDateTime.now());
		message.setNumberOfSends(message.getNumberOfSends()+1);
		message.setDescription("message forwarded to gupshup");
		
		// push result to queue
		rabbitTemplate.convertAndSend(
				configs.getMessagingExchange(), // messaging exchange
				configs.getQueueDeliveryReport(), // dlr routing key
				message  // message 
		);
	}
}
