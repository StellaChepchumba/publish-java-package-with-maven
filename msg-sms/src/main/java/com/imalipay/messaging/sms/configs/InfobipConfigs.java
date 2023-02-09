package com.imalipay.messaging.sms.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.infobip.ApiClient;
import com.infobip.api.SendSmsApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@AllArgsConstructor
public class InfobipConfigs 
{
	private final ApplicationConfigs configs;
	
	public static ApiClient createApiClient(
		String apiKeyPrefix, 
		String apiKey, 
		String baseUrl
	) 
	{
		var apiClient = new ApiClient();
	    apiClient.setApiKeyPrefix(apiKeyPrefix);
	    apiClient.setApiKey(apiKey);
	    apiClient.setBasePath(baseUrl);
	    
	    return apiClient;
	} 
	
	@Bean("nigeria")
	public SendSmsApi createNigeriaSendSmsApi()
	{
		var apiClient =  createApiClient(
				configs.getApiKeyPrefix(), 
				configs.getKey(), 
				configs.getBaseUrl());
		
		return new SendSmsApi(apiClient);
	}
	
	@Bean("southAfrica")
	public SendSmsApi createSouthAfricaSendSmsApi()
	{
		var apiClient =  createApiClient(
				configs.getSaApiKeyPrefix(), 
				configs.getSaKey(), 
				configs.getSaBaseUrl());
		
		return new SendSmsApi(apiClient);
	}
}
