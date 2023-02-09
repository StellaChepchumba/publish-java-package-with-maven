package com.imalipay.messaging.core.controllers;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imalipay.messaging.library.dtos.AITConstants;
import com.imalipay.messaging.core.dtos.AITRequestDTO;
import com.imalipay.messaging.core.models.Routes;
import com.imalipay.messaging.core.services.BotpressService;
import com.imalipay.messaging.core.services.OtpService;
import com.imalipay.messaging.core.services.RoutesService;
import com.imalipay.messaging.library.dtos.DeliveryMode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class AITController 
{

	private final RoutesService routesService;
	private final BotpressService botpressService;
	private final OtpService otpService;
	
	// endpoints 
	private static final String AIT_USSD = "/{countryCode}/messaging/ussd/provider/ait";
	private static final String AIT_SMS_DLR = "/{countryCode}/messaging/sms/provider/ait";
	
	@PostMapping(
		path = AIT_USSD,
		consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public String handleUSSDMessage(
		@Valid AITRequestDTO message,
		@PathVariable(required = true) String countryCode
	) throws JsonProcessingException 
	{
		var mode = DeliveryMode.BOT;
		log.debug("received message from AIT in {}: {}", countryCode, message);
		
		// add default if text is empty
		if (message.getText().isBlank()) 
		{
			log.trace("message is empty, will add default");
			message.setText(message.getServiceCode());
		}

		try {
			
			// check otp verification service
			if (otpService.isOtpCode(otpUssdFormatter(message.getServiceCode(), message.getText()), countryCode.toUpperCase()))
			{
				log.trace("verifying otps tied to {}", message.getPhoneNumber().substring(1).trim());
				otpService.verifyOtp(
						message.getPhoneNumber().substring(1).trim(), 
						countryCode);
				return "END Your OTP has been successfully verified";
			}
			
			log.trace("search for route for message");
			Routes route = routesService.findRoute(mode, countryCode.toUpperCase());
			log.trace("search for route for message");
			
			var botId = route.getExtraData().get(AITConstants.BOTPRESS_BOT_ID);
			
			log.trace("ussd {} bot id {}", countryCode, botId);
			var response = botpressService.sendBotpressMessage(
					textParser(message.getText()), 
					message.getPhoneNumber().substring(1), // format msisdn remove leading + 
					botId);
		    return generateSessionResponse(botpressService.aggregateTextResponses(response));
		} catch (Exception e) {
			log.error(e.getMessage());
			return generateSessionResponse("Dear customer, kindly try again later, or contact our customer support for assistance");
		}
		
	}
	
	@PostMapping(path = AIT_SMS_DLR)
	public String handleSmsMessage(
		   String message,
		@PathVariable(required = true) String countryCode
	) throws JsonProcessingException 
	{
		log.debug("received sms dlr from AIT in {}: {}", countryCode, message);
		return "";
	}
	
	private String textParser(String text)
	{
		var responses = text.split("\\*");
		
		return responses[responses.length - 1].trim();
	}
	
	private String generateSessionResponse(String response)
	{
		if(response.isEmpty())
		{
			return "END Thank you for using Imalipay";
		}
		
		if(response.startsWith("CON ") || response.startsWith("END "))
		{
			return response;
		}
		
		return "CON "+ response;
	}
	
	private String otpUssdFormatter(String serviceCode, String text)
	{
		var formatedServiceCode = new String();
		// remove trailing #
		formatedServiceCode = serviceCode.substring(0, serviceCode.length() - 1);
		
		// add trailing * 
		formatedServiceCode = formatedServiceCode + "*";
		
		// add text 
		formatedServiceCode = formatedServiceCode + text;
		
		// add trailing #  
		formatedServiceCode = formatedServiceCode + "#";
		
		log.debug("formatted service code {}", formatedServiceCode);
		
		return formatedServiceCode;
	}
}
