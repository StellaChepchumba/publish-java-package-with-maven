package com.imalipay.messaging.push_notification.listeners;

import java.time.ZonedDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;
import com.imalipay.messaging.push_notification.configs.ApplicationConfigs;
import com.imalipay.messaging.push_notification.services.FirebaseService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class FirebaseListener 
{
	private final RabbitTemplate rabbitTemplate;
	private final FirebaseService firebaseService;
	private final ApplicationConfigs configs;
	
	@RabbitListener(queues = "${imalipay.messaging.out.push_notification.firebase}")
	public void recievedMessage(MessageDTO message) throws FirebaseMessagingException 
	{
		log.info("recieved firebase message From rabbitMQ: {}", message);
		
		var response = firebaseService.sendNotification(message);
		
		log.debug("notification sent message id {}", response);
		
		// update message 
		message.setExternalId(response);
		message.setStatus(MessageStatus.SENT);
		message.setSentAt(ZonedDateTime.now());
		message.setNumberOfSends(message.getNumberOfSends()+1);
		message.setDescription("message forwarded to Firebase");
		
		// push result to queue
		rabbitTemplate.convertAndSend(
				configs.getMessagingExchange(), // messaging exchange
				configs.getQueueDeliveryReport(), // dlr routing key
				message  // message 
		);
	}
}
