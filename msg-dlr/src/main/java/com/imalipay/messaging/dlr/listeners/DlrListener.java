package com.imalipay.messaging.dlr.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.dlr.service.DlrService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class DlrListener 
{
	private final DlrService dlrService;
	
	@RabbitListener(queues = "${imalipay.messaging.queue.dlr}")
	public void recievedMessage(MessageDTO message)  
	{
		log.trace("recieved dlr message: {}", message);
		
		dlrService.updateMessgaeStatus(message);
		
		log.trace("dlr message logged");
	}
}
