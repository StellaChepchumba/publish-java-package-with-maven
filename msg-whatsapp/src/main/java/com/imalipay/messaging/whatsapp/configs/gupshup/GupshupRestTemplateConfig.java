package com.imalipay.messaging.whatsapp.configs.gupshup;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.imalipay.messaging.whatsapp.configs.ApplicationConfigs;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
public class GupshupRestTemplateConfig
{
	private final ApplicationConfigs config;
	
	@Bean
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager()
	{
		PoolingHttpClientConnectionManager result = 
				new PoolingHttpClientConnectionManager();
		result.setMaxTotal(config.getMaxConnections());
        result.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());
		return result;
	}

	@Bean
	public RequestConfig requestConfig()
	{
		return RequestConfig
					.custom()
					.setConnectionRequestTimeout(config.getRequestTimeout())
					.setConnectTimeout(config.getConnectionTimeout())
					.setSocketTimeout(config.getReadTimeout())
					.build();
	}

	@Bean
	public CloseableHttpClient httpClient(
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
		RequestConfig requestConfig
	)
	{
		return HttpClientBuilder
					.create()
					.setConnectionManager(poolingHttpClientConnectionManager)
					.setDefaultRequestConfig(requestConfig)
					.build();
	}

	@Bean(name="gupshupRestTemplate")
	public RestTemplate restTemplate(HttpClient httpClient)
	{
		var restTemplate=  new RestTemplate();
		
		var interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) 
        {
        	interceptors = new ArrayList<>();
        }
        interceptors.add(new HttpInterceptor());
        restTemplate.setInterceptors(interceptors);
        
		var requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        
      
		return restTemplate;
	}   
	
	class HttpInterceptor implements ClientHttpRequestInterceptor 
	{
		Logger log = LoggerFactory.getLogger(HttpInterceptor.class);
		@Override
		public ClientHttpResponse intercept(
				HttpRequest request, byte[] body,
				ClientHttpRequestExecution execution
		) throws IOException {
			HttpHeaders headers = request.getHeaders();
			headers.add("apikey", config.getGupshupApiKey());
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setAccept(List.of(MediaType.APPLICATION_JSON));
			
			log.trace("request headers {}", headers);
			return execution.execute(request, body);

		}

	}
}
