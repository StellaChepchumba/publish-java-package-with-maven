package com.imalipay.messaging.core.services;

import javax.transaction.Transactional;

import static org.springframework.data.jpa.domain.Specification.*;

import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.core.configs.ApplicationConfigs;
import com.imalipay.messaging.core.models.Messages;
import com.imalipay.messaging.core.models.Routes;
import com.imalipay.messaging.core.repositories.MessageTemplatesRepository;
import com.imalipay.messaging.core.repositories.MessagesRepositories;
import com.imalipay.messaging.core.repositories.specifications.ExampleSpecification;
import com.imalipay.messaging.core.repositories.specifications.RangeSpecification;
import com.imalipay.messaging.core.repositories.specifications.ZonedDateTimeRange;
import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.imalipay.messaging.library.dtos.EmailMessageDTO;
import com.imalipay.messaging.library.dtos.Events;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;
import com.imalipay.messaging.library.dtos.SendBatchMessageDTO;
import com.imalipay.messaging.library.dtos.SendEmailDTO;
import com.imalipay.messaging.library.dtos.SendEventMessageDTO;
import com.imalipay.messaging.library.dtos.SendMessageDTO;
import com.imalipay.messaging.library.dtos.SendNotificationDTO;
import com.imalipay.messaging.library.utils.Utils;

import freemarker.template.Configuration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class MessagingService 
{
	
	private final RabbitTemplate rabbitTemplate;
	private final MessagesRepositories messagesRepositories;
	private final MessageTemplatesRepository messageTemplatesRepository;
	private final RoutesService routesService;
	private final ApplicationConfigs applicationConfigs;
	private final Configuration config;
	private final ObjectMapper mapper;
	
	@Transactional(rollbackOn = Exception.class)
	public void logMessage(
		SendMessageDTO message, 
		DeliveryMode deliveryMode, 
		String countryCode
	) throws Exception 
	{
		log.debug("processing message request");
		
		log.trace("search for route for message");
		Routes route = routesService.findRoute(deliveryMode, countryCode);
		log.trace("route found {}", route);
		
		if (deliveryMode.equals(DeliveryMode.SMS)) 
		{
			message.setTo(Utils.msisdnFormate(message.getTo(), countryCode));
		}
		
		log.debug("build message to send");
		Messages messageEntity = Messages.builder()
				.messageId(UUID.randomUUID())
				.body(message.getBody())
				.countryCode(countryCode)
				.source(message.getFrom())
				.recipient(message.getTo())
				.route(route)
				.status(MessageStatus.CREATED)
				.createdAt(ZonedDateTime.now())
				.description("logged message for processing")
				.build();
		
		var extraData = route.getExtraData();
		
		if(message.getExtraData() != null)
		{
			extraData.putAll(message.getExtraData());
		}
		
		
		MessageDTO messageDTO =  MessageDTO.builder()
				.body(messageEntity.getBody())
				.countryCode(messageEntity.getCountryCode())
				.deliveryMode(messageEntity.getRoute().getDeliveryMode())
				.from(messageEntity.getSource())
				.to(messageEntity.getRecipient())
				.status(messageEntity.getStatus())
				.numberOfSends(0)
				.maxNumberOfSends(applicationConfigs.getMaxSendAttempts())
				.extraData(extraData)
				.messageId(messageEntity.getMessageId())
				.vendorCode(route.getVendor().getVendorCode())
				.routeId(route.getRouteId())
				.createdAt(messageEntity.getCreatedAt())
				.description(messageEntity.getDescription())
				.build();
		
		String queue = String.format("com.imalipay.messaging.out.%s.%s",
				route.getDeliveryMode().toString().toLowerCase(),
				route.getVendor().getVendorCode().toLowerCase());
		
		log.debug("log message {} to queue {}", messageDTO, queue);
		
		// log to queue
		rabbitTemplate.convertAndSend(
				applicationConfigs.getMessagingExchange(), // messaging exchange
				queue, // delivery mode routing key
				messageDTO  // message 
		);
		
		// log to database record keeping
		messagesRepositories.save(messageEntity);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void logBatchMessage(
		SendBatchMessageDTO messages, 
		DeliveryMode deliveryMode, 
		String countryCode
	) throws Exception 
	{
		log.debug("processing message request");
		
		log.debug("search for route for message");
		Routes route = routesService.findRoute(deliveryMode, countryCode);
		log.debug("route found {}", route);
		
		var batchMessages = new ArrayList<Messages>();
		messages.getTo().stream()
			.forEach(to -> {

				log.debug("build message to send");
				Messages messageEntity = Messages.builder()
						.messageId(UUID.randomUUID())
						.body(messages.getBody())
						.countryCode(countryCode)
						.source(messages.getFrom())
						.recipient(to)
						.route(route)
						.status(MessageStatus.CREATED)
						.createdAt(ZonedDateTime.now())
						.description("logged message for processing")
						.build();
				
				var extraData = route.getExtraData();
				
				if(messages.getExtraData() != null)
				{
					extraData.putAll(messages.getExtraData());
				}
				
				MessageDTO messageDTO =  MessageDTO.builder()
						.body(messageEntity.getBody())
						.countryCode(messageEntity.getCountryCode())
						.deliveryMode(messageEntity.getRoute().getDeliveryMode())
						.from(messageEntity.getSource())
						.to(messageEntity.getRecipient())
						.status(messageEntity.getStatus())
						.numberOfSends(0)
						.maxNumberOfSends(applicationConfigs.getMaxSendAttempts())
						.extraData(messages.getExtraData())
						.messageId(messageEntity.getMessageId())
						.vendorCode(route.getVendor().getVendorCode())
						.routeId(route.getRouteId())
						.createdAt(messageEntity.getCreatedAt())
						.description(messageEntity.getDescription())
						.build();
				
				String queue = String.format("com.imalipay.messaging.out.%s.%s",
						route.getDeliveryMode().toString().toLowerCase(),
						route.getVendor().getVendorCode().toLowerCase());
				
				log.debug("log message {} to queue {}", messageDTO, queue);
				
				// log to queue
				rabbitTemplate.convertAndSend(
						applicationConfigs.getMessagingExchange(), // messaging exchange
						queue, // delivery mode routing key
						messageDTO  // message 
				);
			});
		
		// log to database record keeping
		messagesRepositories.saveAll(batchMessages);
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void logEventNotificationMessage(
		SendNotificationDTO message, 
		String countryCode
	) 
	{
		log.debug("processing notification requests");
		
		message.getTo().forEach((deliveryMode, to) -> {
			
			var content = SendEventMessageDTO.builder()
					.extraData(message.getMetaData())
					.to(to)
					.build();
			
			 try {
				logEventMessage(
					content, 
					message.getEvent(), 
					deliveryMode, 
					countryCode);
			} catch (Exception e) {
				log.error("failed to process message {}", e);
			}
		});
	}

	@Transactional(rollbackOn = Exception.class)
	public void logEventMessage(
		SendEventMessageDTO message, 
		Events event, 
		DeliveryMode deliveryMode, 
		String countryCode
	) throws Exception 
	{
		log.debug("processing event message requests");
		log.debug("search for message template");
		var templateOptional = messageTemplatesRepository.findByEventAndDeliveryModeAndCountryCode(
				event, deliveryMode, countryCode);
		
		if (templateOptional.isEmpty()) 
		{
			log.error("message template not found");
			throw new RuntimeException("message template not found");
		}
		var templateData = templateOptional.get();
		var template = config.getTemplate(templateData.getMessageTemplateId().toString()); 
		
		StringWriter stringWriter = new StringWriter();
		template.process(message.getExtraData(), stringWriter);
		
		logMessage(
			SendMessageDTO.builder()
			.body(stringWriter.toString())
			.from(templateData.getSource())
			.to(message.getTo())
			.extraData(message.getExtraData())
			.build(), 
			deliveryMode, 
			countryCode);
	}

	public Page<Messages> search(
		Pageable page, 
		Messages exampleMessage, 
		ZonedDateTimeRange zonedDateTimeRange,
		String countryCode
	) 
	{
		exampleMessage.setCountryCode(countryCode);
		Specification<Messages> exampleSpecification = 
				new ExampleSpecification<Messages>()
					.getSpecificationFromExample(Example.of(exampleMessage));
		
		Specification<Messages> rangeSpecification =
				new RangeSpecification<Messages>()
					.withInRange(zonedDateTimeRange);
		
		return messagesRepositories.findAll(
				where(rangeSpecification).and(exampleSpecification),
				page);
		
	}

	public void logEmailMessage(
		SendEmailDTO message, 
		String countryCode
	) throws Exception
	{
		log.debug("processing email request");
		
		log.trace("search for route for message");
		Routes route = routesService.findRoute(DeliveryMode.EMAIL, countryCode);
		log.trace("route found {}", route);
		
		log.trace("build message entity");
		Messages messageEntity = Messages.builder()
				.messageId(UUID.randomUUID())
				.body(message.getBody())
				.countryCode(countryCode)
				.source("no_reply@imalipay.com") // TODO source address mapper 
				.recipient(mapper.writer().writeValueAsString(message.getTo()))
				.route(route)
				.status(MessageStatus.CREATED)
				.createdAt(ZonedDateTime.now())
				.description(message.getDescription())
				.build();
		
		var extraData = route.getExtraData();
		
		if(message.getMetaData() != null)
		{
			extraData.putAll(message.getMetaData());
		}
		extraData.put("cc", mapper.writer().writeValueAsString(message.getCc()));
		extraData.put("bcc", mapper.writer().writeValueAsString(message.getBcc()));
		extraData.put("attachments", mapper.writer().writeValueAsString(message.getAttachments()));
		
		
		EmailMessageDTO messageDTO =  EmailMessageDTO.builder()
				.body(messageEntity.getBody())
				.countryCode(messageEntity.getCountryCode())
				.deliveryMode(messageEntity.getRoute().getDeliveryMode())
				.subject(message.getSubject())
				.from(messageEntity.getSource())
				.to(message.getTo())
				.cc(message.getCc())
				.bcc(message.getBcc())
				.attachments(message.getAttachments())
				.status(messageEntity.getStatus())
				.numberOfSends(0)
				.maxNumberOfSends(applicationConfigs.getMaxSendAttempts())
				.metaData(extraData)
				.messageId(messageEntity.getMessageId())
				.vendorCode(route.getVendor().getVendorCode())
				.routeId(route.getRouteId())
				.createdAt(messageEntity.getCreatedAt())
				.description(messageEntity.getDescription())
				.build();
		
		String queue = String.format("com.imalipay.messaging.out.%s.%s",
				route.getDeliveryMode().toString().toLowerCase(),
				route.getVendor().getVendorCode().toLowerCase());
		
		log.debug("log message {} to queue {}", messageDTO, queue);
		
		// log to queue
		rabbitTemplate.convertAndSend(
				applicationConfigs.getMessagingExchange(), // messaging exchange
				queue, // delivery mode routing key
				messageDTO  // message 
		);
		
		// log to database record keeping
		log.trace("saving message entity {}", messageEntity);
		messagesRepositories.save(messageEntity);
		
	}
	
}
