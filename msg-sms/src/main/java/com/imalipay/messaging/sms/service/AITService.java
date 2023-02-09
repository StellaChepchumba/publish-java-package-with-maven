package com.imalipay.messaging.sms.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.sms.configs.ApplicationConfigs;
import com.imalipay.messaging.sms.dtos.AITSmsResponseDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AITService 
{
	private final RestTemplate restTemplate = new RestTemplate();
	private final ApplicationConfigs configs;
	private final ObjectMapper mapper;
	
	public AITSmsResponseDTO sendTextMessage(
		String message, 
		String sourceAddress, 
		String destinationAddress
	) throws JsonProcessingException
	{

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString()); 
		headers.add("apiKey", configs.getAitApiKey());
		
		var requestBody = new LinkedMultiValueMap<String, String>();
		requestBody.add("username", configs.getAitUsername());
		requestBody.add("to", destinationAddress);
//		requestBody.add("from", sourceAddress);
		requestBody.add("message", message);

		
		log.trace("ait sms request {}", requestBody);
		
		var stringResponse = restTemplate.postForObject(
				configs.getAitHost(), 
				new HttpEntity<LinkedMultiValueMap<String, String>>(requestBody, headers), 
				String.class);
		
		log.trace("ait sms response {}", stringResponse);
		
		return mapper.readValue(stringResponse, AITSmsResponseDTO.class);

	}
}
