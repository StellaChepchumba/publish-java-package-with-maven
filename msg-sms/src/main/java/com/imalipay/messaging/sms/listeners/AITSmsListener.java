package com.imalipay.messaging.sms.listeners;

import java.time.ZonedDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;
import com.imalipay.messaging.sms.configs.ApplicationConfigs;
import com.imalipay.messaging.sms.service.AITService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class AITSmsListener 
{
	private final ApplicationConfigs configs;
	private final RabbitTemplate rabbitTemplate;
	private final AITService aitService;
	
	@RabbitListener(queues = "${imalipay.messaging.out.sms.ait}")
	public void recievedMessage(MessageDTO message) throws JsonProcessingException 
	{
		log.trace("recieved ait message: {}", message);
		
		var response = aitService.sendTextMessage(
				 message.getBody(), 
				 message.getFrom(), 
				 message.getTo());
		
		if (response == null ) 
		{
			message.setStatus(MessageStatus.FAILED);
			message.setSentAt(ZonedDateTime.now());
			message.setNumberOfSends(message.getNumberOfSends()+1);
			message.setDescription("null response submitting message");
			
			// push result to queue
			rabbitTemplate.convertAndSend(
					configs.getMessagingExchange(), // messaging exchange
					configs.getQueueDeliveryReport(), // dlr routing key
					message  // message 
			);
		}
		
		response.getSmsMessageData().getRecipients().forEach( item -> {

			// update message 
			message.setExternalId(item.getMessageId());
			message.setSentAt(ZonedDateTime.now());
			message.setNumberOfSends(message.getNumberOfSends()+1);
			if (item.getStatusCode().equals("101")) {
				message.setStatus(MessageStatus.SENT);
				message.setDescription("message forwarded to ait");
			} else {
				message.setStatus(MessageStatus.FAILED);
				message.setDescription(item.getStatus());
			}
			
			// push result to queue
			rabbitTemplate.convertAndSend(
					configs.getMessagingExchange(), // messaging exchange
					configs.getQueueDeliveryReport(), // dlr routing key
					message  // message 
			);
		});
		
		
		
		
	}
}
