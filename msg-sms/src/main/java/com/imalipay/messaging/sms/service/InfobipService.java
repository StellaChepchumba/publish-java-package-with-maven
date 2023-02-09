package com.imalipay.messaging.sms.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.infobip.ApiException;
import com.infobip.api.SendSmsApi;
import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsTextualMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InfobipService 
{
	private final SendSmsApi nigeriaSmsApi;
	private final SendSmsApi southAfricaSmsApi;
	
	public InfobipService(
		@Qualifier("nigeria") SendSmsApi nigeriaSmsApi,
		@Qualifier("southAfrica") SendSmsApi southAfricaSmsApi)
	{
		this.nigeriaSmsApi = nigeriaSmsApi;
		this.southAfricaSmsApi = southAfricaSmsApi;
	}
	
	
	public String sendTextMessage(
		String message, 
		String sourceAddress, 
		String destinationAddress,
		SendSmsApi sendSmsApi
	) throws ApiException
	{

	    var smsMessage = new SmsTextualMessage()
	        .from(sourceAddress)
	        .addDestinationsItem(new SmsDestination().to(destinationAddress))
	        .text(message);
		log.trace("sending infobip request {}", smsMessage);

	    var smsMessageRequest = new SmsAdvancedTextualRequest().messages(
	        Collections.singletonList(smsMessage)
	    );
	    
	    var smsResponse = sendSmsApi.sendSmsMessage(smsMessageRequest);
		log.trace("received infobip response {}", smsResponse);
	    var messageId = smsResponse.getMessages().get(0).getMessageId();
	    
	    return messageId;
	}
	
	public SendSmsApi getSession(String countryCode)
	{
		log.trace("getting infobip http session for {}", countryCode);
		
		if (countryCode.equalsIgnoreCase("NG")) {
			return nigeriaSmsApi;
		}
		if (countryCode.equalsIgnoreCase("ZA")) {
			return southAfricaSmsApi;
		}
		
		throw new RuntimeException(String.format("no infobip session for %s", countryCode));
	}
}
