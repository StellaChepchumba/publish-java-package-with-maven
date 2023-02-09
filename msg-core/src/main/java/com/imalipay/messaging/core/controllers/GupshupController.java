package com.imalipay.messaging.core.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.core.dtos.GupshupDTO;
import com.imalipay.messaging.core.services.GupshupService;
import com.imalipay.messaging.core.services.MessagingService;
import com.imalipay.messaging.library.dtos.DeliveryMode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class GupshupController 
{	
	// endpoints 
	private static final String RECEIVE_WHATSAPP_MESSAGE = "/{countryCode}/messaging/whatsapp/provider/gupshup";

	private final ObjectMapper mapper;
	private final MessagingService messagingService;
	private final GupshupService gupshupService;
	
	@GetMapping(value = RECEIVE_WHATSAPP_MESSAGE)
	public String receiveMessage() throws Exception
	{
		log.debug("received get request message from gupshup");
		
		return "";
	}
	
	@PostMapping(value = RECEIVE_WHATSAPP_MESSAGE)
	public String receiveWhastappMessage(
		@PathVariable(required = true) String countryCode,
		@RequestBody String receive) throws Exception
	{
		var mode = DeliveryMode.BOT;
		log.debug("received post request message from gupshup: {}", receive);
		
		// parse request data 
		var request = mapper.readValue(receive, GupshupDTO.class);
		
		// skip for gupshup events
		if (!request.getType().equals("message"))
		{
			log.trace("received gupshup event nothing to do");
			return "";
		}

		try {
			// map to message 
			var message = gupshupService.mapper(request, mode, countryCode.toUpperCase());
			
			messagingService.logMessage(
					message, 
					DeliveryMode.BOT, 
					countryCode.toUpperCase());

			return "";
		} catch (Exception e) {
			log.error("error processing message {}", e.getMessage());
			return "";
		}
		
	}
}
