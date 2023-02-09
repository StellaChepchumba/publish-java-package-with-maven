package com.imalipay.messaging.core.configs;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
public class RabbitmqConfigs 
{
	@Bean
	public RabbitTemplate rabbitTemplate(
		ConnectionFactory connectionFactory,
		Jackson2JsonMessageConverter converter) 
	{
	    final var rabbitTemplate = new RabbitTemplate();
	    rabbitTemplate.setMessageConverter(converter);
	    rabbitTemplate.setConnectionFactory(connectionFactory);
	    return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter(
			ObjectMapper objectMapper) 
	{
	    return new Jackson2JsonMessageConverter(objectMapper);
	}
}
