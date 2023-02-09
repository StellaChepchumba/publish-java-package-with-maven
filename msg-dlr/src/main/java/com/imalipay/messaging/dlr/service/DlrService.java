package com.imalipay.messaging.dlr.service;

import org.springframework.stereotype.Service;

import com.imalipay.messaging.dlr.repositories.MessagesRepositories;
import com.imalipay.messaging.dlr.models.Messages;
import com.imalipay.messaging.library.dtos.MessageDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class DlrService 
{
	private final MessagesRepositories messagesRepositories;

	public void updateMessgaeStatus(MessageDTO message) 
	{
		log.trace("update message final status");
		// find existing message
		var optionalMessage = messagesRepositories.findById(message.getMessageId());
		
		if (optionalMessage.isEmpty()) 
		{
			log.error("message not found with id {}", message.getMessageId());
			return;
		}
		log.trace("message found updating");
		
		// log to database record keeping
		messagesRepositories.save(mapper(message));
		
	}
	
	private Messages mapper(MessageDTO message)
	{
		return Messages.builder()
				.messageId(message.getMessageId())
				.externalId(message.getExternalId())
				.body(message.getBody())
				.countryCode(message.getCountryCode())
				.source(message.getFrom())
				.recipient(message.getTo())
				.routeId(message.getRouteId())
				.status(message.getStatus())
				.createdAt(message.getCreatedAt())
				.sentAt(message.getSentAt())
				.numberOfSends(message.getNumberOfSends())
				.description(message.getDescription())
				.build();
	}
}
