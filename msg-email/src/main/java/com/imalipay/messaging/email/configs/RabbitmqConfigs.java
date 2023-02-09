package com.imalipay.messaging.email.configs;

import java.io.IOException;
import java.time.ZonedDateTime;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;

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

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
		ErrorHandler errorHandler,
		ConnectionFactory connectionFactory,
		SimpleRabbitListenerContainerFactoryConfigurer configurer
	) 
	{
	      var factory = new SimpleRabbitListenerContainerFactory();
	      configurer.configure(factory, connectionFactory);
	      factory.setErrorHandler(errorHandler);
	      return factory;
	}
	
	@Bean
	public ErrorHandler errorHandler(
		RabbitTemplate rabbitTemplate, 
		ApplicationConfigs configs, 
		ObjectMapper objectMapper,
		FatalExceptionStrategy exceptionStrategy
	) 
	{
	    return new ImaliPayErrorHandler(rabbitTemplate, configs, objectMapper, exceptionStrategy);
	}
	
	@Bean
	public FatalExceptionStrategy customExceptionStrategy() 
	{
	    return new ConditionalRejectingErrorHandler.DefaultExceptionStrategy() 
	    {
	    	@Override
	        public boolean isFatal(Throwable t) 
	    	{
	            return (t.getCause() instanceof Throwable);
	        }
	    	
	    };
	}
	
	class ImaliPayErrorHandler extends ConditionalRejectingErrorHandler
	{
		private final RabbitTemplate rabbitTemplate;
		private final ApplicationConfigs configs;
		private final ObjectMapper objectMapper;
		private final FatalExceptionStrategy exceptionStrategy;
		
		ImaliPayErrorHandler(
			RabbitTemplate rabbitTemplate, 
			ApplicationConfigs configs, 
			ObjectMapper objectMapper,
			FatalExceptionStrategy exceptionStrategy
		)
		{
			this.configs = configs;
			this.rabbitTemplate = rabbitTemplate;
			this.objectMapper = objectMapper;
			this.exceptionStrategy = exceptionStrategy;
		}
		
		@Override
		public void handleError(Throwable t) 
		{
			log(t);
			if (!super.causeChainContainsARADRE(t) && this.exceptionStrategy.isFatal(t)) 
			{
				if (t instanceof ListenerExecutionFailedException) 
				{
					Message failed = ((ListenerExecutionFailedException) t).getFailedMessage();
					handleDiscarded(failed);
					throw new ImmediateAcknowledgeAmqpException("Fatal incident while processing message");
				}
				
				throw new AmqpRejectAndDontRequeueException(
						"Error Handler converted exception to fatal", 
						true,
						t);
			}
		}
		
		@Override
		public void handleDiscarded(Message failed) 
    	{
			try {
				var message = objectMapper.readValue(failed.getBody(), MessageDTO.class);

				message.setSentAt(ZonedDateTime.now());
				message.setStatus(MessageStatus.FAILED);
				message.setNumberOfSends(message.getNumberOfSends()+1);
				logger.error("publishing failed messaged to dlr queue");
				// push result to queue
				rabbitTemplate.convertAndSend(
						configs.getMessagingExchange(), // messaging exchange
						configs.getQueueDeliveryReport(), // dlr routing key
						message  // message 
				);
			} catch (IOException e) {
				logger.error("error logging message to dlr queue");
			}
    	}
	}
}
