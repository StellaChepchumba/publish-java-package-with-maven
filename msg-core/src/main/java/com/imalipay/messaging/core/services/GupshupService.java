package com.imalipay.messaging.core.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.imalipay.messaging.library.dtos.GupshupConstants;
import com.imalipay.messaging.core.configs.GupshupMessageTypes;
import com.imalipay.messaging.core.dtos.GupshupDTO;
import com.imalipay.messaging.core.models.Routes;
import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.imalipay.messaging.library.dtos.SendMessageDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GupshupService 
{

	private final RoutesService routesService;
	
	public SendMessageDTO mapper(
		GupshupDTO gupshupMessage, 
		DeliveryMode deliveryMode, 
		String countryCode
	) throws Exception
	{
		log.trace("search for route for message");
		Routes route = routesService.findRoute(deliveryMode, countryCode);
		log.trace("found for route for message {}", route);

		
		log.trace("converting {} to send message dto", gupshupMessage);

		var message = SendMessageDTO.builder()
			.from(gupshupMessage.getPayload().getSender().getPhone())
			.to(gupshupMessage.getApp())
			.body(getMessageBody(gupshupMessage))
			.extraData(getExtraData(gupshupMessage, route))
			.build();
		
		log.trace("converted message {}", message);
		
		return message;
	}
	
	private String getMessageBody(GupshupDTO gsMessage)
	{
		log.trace("extracting message body");
		if (GupshupMessageTypes.text.equals(gsMessage.getPayload().getType())) 
		{
			var messageBody = gsMessage.getPayload().getPayload().get("text");
			log.trace("message body: {}", messageBody);
			return messageBody;
		} else if (GupshupMessageTypes.image.equals(gsMessage.getPayload().getType())) {
			var messageBody = gsMessage.getPayload().getPayload().get("url");
			log.trace("message body: {}", messageBody);
			return messageBody;
		} else {
			log.error("unsupported message type: {}", gsMessage.getType());
			throw new RuntimeException("unsupported message type");
		}
	}
	
	private Map<String, String> getExtraData(GupshupDTO gsMessage, Routes route)
	{
		log.trace("generating extra data");
		var extraData = new HashMap<String, String>();
		extraData.put(GupshupConstants.MESSAGE_TYPE, gsMessage.getPayload().getType().toString());
		
		if (!route.getExtraData().get(GupshupConstants.GUPSHUP_APP_NAME).equalsIgnoreCase(gsMessage.getApp())) 
		{
			log.error("unsupported bot: {}", gsMessage.getApp());
			throw new RuntimeException("unsupported bot");	
		}
		
		return extraData;
	}
}
