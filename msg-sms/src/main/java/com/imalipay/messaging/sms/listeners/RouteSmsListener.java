package com.imalipay.messaging.sms.listeners;

import java.time.ZonedDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;
import com.imalipay.messaging.sms.configs.ApplicationConfigs;
import com.imalipay.messaging.sms.service.RoutesmsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class RouteSmsListener 
{
	private final ApplicationConfigs configs;
	private final RabbitTemplate rabbitTemplate;
	private final RoutesmsService routesmsService;
	
	@RabbitListener(queues = "${imalipay.messaging.out.sms.routesms}")
	public void recievedMessage(MessageDTO message) throws RecoverablePduException, UnrecoverablePduException, SmppTimeoutException, SmppChannelException, InterruptedException 
	{
		log.trace("recieved routesms message: {}", message);
		
		var messageId = routesmsService.sendTextMessage(
				 message.getBody(), 
				 message.getFrom(), 
				 message.getTo());
		
		// update message 
		message.setExternalId(messageId);
		message.setStatus(MessageStatus.SENT);
		message.setSentAt(ZonedDateTime.now());
		message.setNumberOfSends(message.getNumberOfSends()+1);
		message.setDescription("message forwarded to routesms");
		
		// push result to queue
		rabbitTemplate.convertAndSend(
				configs.getMessagingExchange(), // messaging exchange
				configs.getQueueDeliveryReport(), // dlr routing key
				message  // message 
		);
		
	}
}
