package com.imalipay.messaging.email.listeners;

import java.time.ZonedDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.imalipay.messaging.email.configs.ApplicationConfigs;
import com.imalipay.messaging.email.services.GmailService;
import com.imalipay.messaging.library.dtos.EmailMessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class GmailListener 
{
	private final GmailService gmailService;
	private final RabbitTemplate rabbitTemplate;
	private final ApplicationConfigs configs;
	
	@RabbitListener(queues = "${imalipay.messaging.out.email.gmail}")
	public void recievedMessage(EmailMessageDTO message) throws Exception 
	{
		log.debug("received email to send {}", message);
		
		gmailService.sendEmailMessage(message);
		
		// update message 
		message.setStatus(MessageStatus.SENT);
		message.setSentAt(ZonedDateTime.now());
		message.setNumberOfSends(message.getNumberOfSends()+1);
		message.setDescription("message forwarded to gmail");
		
		// push result to queue
		log.debug("push message to dlr queue {}", message);
		rabbitTemplate.convertAndSend(
				configs.getMessagingExchange(), // messaging exchange
				configs.getQueueDeliveryReport(), // dlr routing key
				message  // message 
		);
	}
}
