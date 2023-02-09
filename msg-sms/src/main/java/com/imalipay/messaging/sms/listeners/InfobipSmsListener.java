package com.imalipay.messaging.sms.listeners;

import java.time.ZonedDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;
import com.imalipay.messaging.sms.configs.ApplicationConfigs;
import com.imalipay.messaging.sms.service.InfobipService;
import com.infobip.ApiException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class InfobipSmsListener 
{
	private final ApplicationConfigs configs;
	private final RabbitTemplate rabbitTemplate;
	private final InfobipService infobipService;
	
	@RabbitListener(queues = "${imalipay.messaging.out.sms.infobip}")
	public void recievedMessage(MessageDTO message) throws ApiException 
	{
		log.trace("recieved infobip message: {}", message);
		
		var messageId = infobipService.sendTextMessage(
				 message.getBody(), 
				 message.getFrom(), 
				 message.getTo(),
				 infobipService.getSession(message.getCountryCode()));
		
		// update message 
		message.setExternalId(messageId);
		message.setStatus(MessageStatus.SENT);
		message.setSentAt(ZonedDateTime.now());
		message.setNumberOfSends(message.getNumberOfSends()+1);
		message.setDescription("message forwarded to Infobip");
		
		// push result to queue
		rabbitTemplate.convertAndSend(
				configs.getMessagingExchange(), // messaging exchange
				configs.getQueueDeliveryReport(), // dlr routing key
				message  // message 
		);
		
	}
}
