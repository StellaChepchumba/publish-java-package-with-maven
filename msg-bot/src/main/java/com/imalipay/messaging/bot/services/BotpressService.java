package com.imalipay.messaging.bot.services;

import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.bot.configs.ApplicationConfigs;
import com.imalipay.messaging.bot.dtos.BotpressRequestDTO;
import com.imalipay.messaging.bot.dtos.BotpressResponseDTO;
import com.imalipay.messaging.bot.dtos.BotpressResponsePayloadDTO;
import com.imalipay.messaging.bot.dtos.ChoiceDTO;
import com.imalipay.messaging.library.dtos.GupshupConstants;
import com.imalipay.messaging.library.dtos.MessageDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class BotpressService 
{
	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper mapper;
	private final ApplicationConfigs configs;
	
	public BotpressResponseDTO sendBotpressMessage(MessageDTO message) throws JsonProcessingException
	{
		log.debug("sending out botpress message {}", message);
		
		var request = BotpressRequestDTO.builder()
				.text(message.getBody())
				.type(getMessageType(message))
				.build();

		log.trace("botpress request: {}", request);
		
		var response = restTemplate.postForObject(
				configs.getBotpressUrl(),
				request, 
				String.class,
				getBotId(message), // botId 
				message.getFrom());
		
		log.trace("botpress response: {}", response);
		
		var botpressResponse = mapper.readValue(response, BotpressResponseDTO.class);
		
		return botpressResponse;
	}
	
	public BotpressResponseDTO sendBotpressMessage(
		String message,String from, String botId) throws JsonProcessingException
	{
		log.debug("sending out botpress message {}", message);
		
		var request = BotpressRequestDTO.builder()
				.text(message)
				.type("text")
				.build();

		log.trace("botpress request: {}", request);
		
		var response = restTemplate.postForObject(
				configs.getBotpressUrl(),
				request, 
				String.class,
				botId,
				from);
		
		log.trace("botpress response: {}", response);
		
		var botpressResponse = mapper.readValue(response, BotpressResponseDTO.class);
		
		return botpressResponse;
	}
	
	public String aggregateTextResponses(BotpressResponseDTO response)
	{
		StringBuilder sb = new StringBuilder();
		
		response.getResponses().stream()
			.filter(item -> item.getType().equals("text"))
			.forEach(item -> sb.append(item.getText()).append("\n"));
		
		return sb.toString();
	}
	
	public String singleChoiceTypeParser(BotpressResponsePayloadDTO botResponse) 
	{
		var response = new StringBuilder();
		response.append(botResponse.getText()).append("\n");
		botResponse.getChoices().stream()
			.sorted(Comparator.comparing(ChoiceDTO::getValue))
			.forEach(item -> { 
				response.append(item.getValue())
					.append(". ")
					.append(item.getTitle())
					.append("\n");
			});
		
		return response.toString();
	}
	
	private String getBotId(MessageDTO message)
	{
		if (message.getExtraData() == null) 
		{
			log.error("bot id missing from botpress message");
			throw new RuntimeException("bot id missing from botpress messageing");
		}
		
		if (!message.getExtraData().containsKey(GupshupConstants.BOTPRESS_BOT_ID)) 
		{
			log.error("bot id missing from botpress message");
			throw new RuntimeException("bot id missing from botpress messageing");
		}
		
		return message.getExtraData().get(GupshupConstants.BOTPRESS_BOT_ID);
		
	}
	
	private String getMessageType(MessageDTO message)
	{
		if (message.getExtraData() == null) 
		{
			log.error("message type missing from botpress message");
			throw new RuntimeException("message type missing from botpress messageing");
		}
		
		if (!message.getExtraData().containsKey(GupshupConstants.MESSAGE_TYPE)) 
		{
			log.error("message type missing from botpress message");
			throw new RuntimeException("message type missing from botpress messageing");
		}
		
		return message.getExtraData().get(GupshupConstants.MESSAGE_TYPE);
		
	}
}
