package com.imalipay.messaging.core.services;

import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.core.configs.ApplicationConfigs;
import com.imalipay.messaging.core.dtos.BotpressRequestDTO;
import com.imalipay.messaging.core.dtos.BotpressResponseDTO;
import com.imalipay.messaging.core.dtos.BotpressResponsePayloadDTO;
import com.imalipay.messaging.core.dtos.ChoiceDTO;

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
				botId, // botId 
				from);
		
		log.trace("botpress response: {}", response);
		
		var botpressResponse = mapper.readValue(response, BotpressResponseDTO.class);
		
		return botpressResponse;
	}
	
	public String aggregateTextResponses(BotpressResponseDTO botResponse)
	{
		var response = new StringBuilder();
		
		botResponse.getResponses().stream()
			.forEach(item -> {
				if (item.getType().equals("single-choice"))
				{
					response.append(singleChoiceTypeParser(item));
				} else if (item.getType().equals("text")) {
					response.append(item.getText()).append("\n");
				}
			});
		
		return response.toString();
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
}