package com.imalipay.messaging.whatsapp.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.whatsapp.configs.ApplicationConfigs;
import com.imalipay.messaging.whatsapp.dtos.gupshup.GupshupFileMessage;
import com.imalipay.messaging.whatsapp.dtos.gupshup.GupshupImageMessage;
import com.imalipay.messaging.whatsapp.dtos.gupshup.GupshupLocationMessage;
import com.imalipay.messaging.whatsapp.dtos.gupshup.GupshupMessageResponse;
import com.imalipay.messaging.whatsapp.dtos.gupshup.GupshupMessageTypes;
import com.imalipay.messaging.whatsapp.dtos.gupshup.GupshupTemplateMessage;
import com.imalipay.messaging.whatsapp.dtos.gupshup.GupshupTextMessage;
import com.imalipay.messaging.whatsapp.dtos.gupshup.GupshupVoiceMessage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GupshupService 
{
	@Qualifier("gupshupRestTemplate")
	private final RestTemplate restTemplate;
	private final ApplicationConfigs configs;
	private final ObjectMapper objectMapper;
	
	public GupshupMessageResponse sendMessage(MessageDTO message) throws Exception
	{
		if(message.getExtraData() == null || !message.getExtraData().containsKey("type"))
		{
			throw new RuntimeException("Gupshup message type missing");
		}
		String type =  message.getExtraData().get("type");
		
		
		if(type.toUpperCase().equals(GupshupMessageTypes.FILE.toString()))
		{
			log.trace("build gupshup whatsapp file message");
			
			var gupshupMessage = GupshupFileMessage.builder()
				.destination(message.getTo())
				.source(message.getFrom())
				.build();
			
			var fileDetails = gupshupMessage.new Message(
					message.getExtraData().get("url"),
					message.getExtraData().get("filename"));
			
			gupshupMessage.setMessage(fileDetails);
			
			return sendFileWhatsappMessage(gupshupMessage);
		}
		
		if(type.toUpperCase().equals(GupshupMessageTypes.IMAGE.toString()))
		{
			log.trace("build gupshup whatsapp image message");
			
			var gupshupMessage = GupshupImageMessage.builder()
				.destination(message.getTo())
				.source(message.getFrom())
				.build();
			
			var fileDetails = gupshupMessage.new Message(
					message.getExtraData().get("originalUrl"),
					message.getExtraData().get("previewUrl"),
					message.getExtraData().get("caption"));
			
			gupshupMessage.setMessage(fileDetails);
			
			return sendImageWhatsappMessage(gupshupMessage);
		}
		
		if(type.toUpperCase().equals(GupshupMessageTypes.LOCATION.toString()))
		{
			log.trace("build gupshup whatsapp location message");
			
			var gupshupMessage = GupshupLocationMessage.builder()
				.destination(message.getTo())
				.source(message.getFrom())
				.build();
			
			var fileDetails = gupshupMessage.new Message(
					Double.valueOf(message.getExtraData().get("longitude")),
					Double.valueOf(message.getExtraData().get("latitude")),
					message.getExtraData().get("name"),
					message.getExtraData().get("address"));
			
			gupshupMessage.setMessage(fileDetails);
			
			return sendLocationWhatsappMessage(gupshupMessage);
		}
		
		if(type.toUpperCase().equals(GupshupMessageTypes.TEMPLATE.toString()))
		{
			log.trace("build gupshup whatsapp template message");
			
			var gupshupMessage = GupshupTemplateMessage.builder()
				.destination(message.getTo())
				.source(message.getFrom())
				.build();

			var fileDetails = gupshupMessage.new Template(
					message.getExtraData().get("gupshupTemplateId"),
					message.getExtraData().get("gupshupTemplateParams").split(","));
			
			gupshupMessage.setTemplate(fileDetails);
			
			return sendTemplateWhatsappMessage(gupshupMessage);
		}
		
		if(type.toUpperCase().equals(GupshupMessageTypes.AUDIO.toString()))
		{
			log.trace("build gupshup whatsapp audio message");
			
			var gupshupMessage = GupshupVoiceMessage.builder()
				.destination(message.getTo())
				.source(message.getFrom())
				.build();
			
			var fileDetails = gupshupMessage.new Message(
					message.getExtraData().get("url"));
			
			gupshupMessage.setMessage(fileDetails);
			
			return sendVoiceWhatsappMessage(gupshupMessage);
		}
		
		if(type.toUpperCase().equals(GupshupMessageTypes.TEXT.toString()))
		{
			log.trace("build gupshup whatsapp text message");
			
			var gupshupMessage = GupshupTextMessage.builder()
				.destination(message.getTo())
				.source(message.getFrom())
				.message(message.getBody())
				.build();
			
			
			return sendTextWhatsappMessage(gupshupMessage);
		}
		
		throw new RuntimeException("message type not found");
	}
	
	public GupshupMessageResponse sendFileWhatsappMessage(
		GupshupFileMessage message
	) throws RestClientException, JsonProcessingException
	{
		log.trace("sending whatsapp file message via gupshup {}", message);

		var stringResponse = restTemplate.postForObject(
				configs.getGupshupUrl(), 
				convertPojoToForm(message),
				String.class);
		
		var response = objectMapper.readValue(stringResponse, GupshupMessageResponse.class);
		
		log.debug("gupshup whatsapp file message response {}", response); 
		return response;
	}
	
	public GupshupMessageResponse sendImageWhatsappMessage(
		GupshupImageMessage message
	) throws RestClientException, JsonProcessingException
	{
		log.trace("sending whatsapp image message via gupshup {}", message);

		var stringResponse = restTemplate.postForObject(
				configs.getGupshupUrl(), 
				convertPojoToForm(message),
				String.class);
		
		var response = objectMapper.readValue(stringResponse, GupshupMessageResponse.class);
		
		log.debug("gupshup whatsapp image message response {}", response); 
		return response;
	}
	
	public GupshupMessageResponse sendLocationWhatsappMessage(
		GupshupLocationMessage message
	) throws RestClientException, JsonProcessingException
	{
		log.trace("sending whatsapp location message via gupshup {}", message);

		var stringResponse = restTemplate.postForObject(
				configs.getGupshupUrl(), 
				convertPojoToForm(message),
				String.class);
		
		var response = objectMapper.readValue(stringResponse, GupshupMessageResponse.class);
		
		log.debug("gupshup whatsapp location message response {}", response); 
		return response;
	}
	
	public GupshupMessageResponse sendTemplateWhatsappMessage(
		GupshupTemplateMessage message
	) throws RestClientException, JsonProcessingException
	{
		log.trace("sending whatsapp template message via gupshup {}", message);

		var stringResponse = restTemplate.postForObject(
				configs.getGupshupUrl(), 
				convertPojoToForm(message),
				String.class);
		
		log.trace("gupshup whatsapp template message raw response {}", stringResponse); 
		
		var response = objectMapper.readValue(stringResponse, GupshupMessageResponse.class);
		
		log.debug("gupshup whatsapp template message response {}", response); 
		return response;
	}

	
	public GupshupMessageResponse sendTextWhatsappMessage(
		GupshupTextMessage message
	) throws RestClientException, JsonProcessingException
	{
		log.trace("sending whatsapp text message via gupshup {}", message);
		
		var stringResponse = restTemplate.postForObject(
				configs.getGupshupUrl(), 
				convertPojoToForm(message),
				String.class);
		
		var response = objectMapper.readValue(stringResponse, GupshupMessageResponse.class);
		
		log.debug("gupshup whatsapp text message response {}", response); 
		return response;
	}
	
	public GupshupMessageResponse sendVoiceWhatsappMessage(
		GupshupVoiceMessage message
	) throws RestClientException, JsonProcessingException
	{
		log.trace("sending whatsapp voice message via gupshup {}", message);
		
		var stringResponse = restTemplate.postForObject(
				configs.getGupshupUrl(), 
				convertPojoToForm(message),
				String.class);
		
		var response = objectMapper.readValue(stringResponse, GupshupMessageResponse.class);
		
		log.debug("gupshup whatsapp voice message response {}", response); 
		return response;
	}
		
	
	private LinkedMultiValueMap<String, Object> convertPojoToForm(Object message) throws JsonProcessingException
	{
		log.trace("mapping object to form");
		var valueMap = new LinkedMultiValueMap<String, Object>();
		Map<String, Object> fieldMap = objectMapper.convertValue(
				message, 
				new TypeReference<Map<String, Object>>() {});
		
		valueMap.setAll(fieldMap);
		
		// escape JSON string 
		valueMap.set("message", objectMapper.writeValueAsString(valueMap.get("message")));
		log.debug("gupshup form request map {}", valueMap);
		return valueMap;
	}
}
