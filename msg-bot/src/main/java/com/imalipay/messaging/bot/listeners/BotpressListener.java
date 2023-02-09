package com.imalipay.messaging.bot.listeners;

import java.time.ZonedDateTime;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.imalipay.messaging.bot.configs.ApplicationConfigs;
import com.imalipay.messaging.bot.services.BotpressService;
import com.imalipay.messaging.bot.services.MessagingService;
import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.imalipay.messaging.library.dtos.GupshupConstants;
import com.imalipay.messaging.library.dtos.GupshupMessageTypes;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class BotpressListener 
{
	private final BotpressService botpressService;
	private final MessagingService messagingervice;
	private final RabbitTemplate rabbitTemplate;
	private final ApplicationConfigs configs;
	
	@RabbitListener(queues = "${imalipay.messaging.out.bot.botpress}")
	public void recievedMessage(MessageDTO message) 
	{
		log.debug("received botpress message to send {}", message);
		message.setSentAt(ZonedDateTime.now());
		message.setNumberOfSends(message.getNumberOfSends()+1);
		
		try {
			var response = botpressService.sendBotpressMessage(message);

			// send response to customer
			response.getResponses().stream()
				.forEach(responseMsg -> {
					if (responseMsg.getType().equals("image")) 
					{
						messagingervice.send(
							message.getFrom(), 
							message.getExtraData().get(GupshupConstants.WHATSAPP_NUMBER),
//							BOT_NUMBER_MAP.get(message.getTo()), 
							GupshupMessageTypes.image.toString(),
//							"image",
							Map.of("type", responseMsg.getType(),
								   "originalUrl", responseMsg.getImage()), 
							message.getCountryCode(), 
							DeliveryMode.WHATSAPP);
						
					} else if (responseMsg.getType().equals("text")) {
						messagingervice.send(
							message.getFrom(), 
							message.getExtraData().get(GupshupConstants.WHATSAPP_NUMBER),
//							BOT_NUMBER_MAP.get(message.getTo()), 
							responseMsg.getText(),
							Map.of("type", responseMsg.getType()), 
							message.getCountryCode(), 
							DeliveryMode.WHATSAPP);
					} else if (responseMsg.getType().equals("single-choice")) {
						messagingervice.send(
							message.getFrom(), 
							message.getExtraData().get(GupshupConstants.WHATSAPP_NUMBER),
//							BOT_NUMBER_MAP.get(message.getTo()), 
							botpressService.singleChoiceTypeParser(responseMsg),
							Map.of("type", "text"), 
							message.getCountryCode(), 
							DeliveryMode.WHATSAPP);
					}
				});
					
			
			// update message 
			message.setStatus(MessageStatus.SENT);
			message.setDescription("message forwarded to botpress");
			
		} catch (JsonProcessingException e) {

			// update message 
			message.setStatus(MessageStatus.FAILED);
			message.setDescription(e.getMessage());
		}
		
		// push result to queue
		log.debug("push message to dlr queue {}", message);
		rabbitTemplate.convertAndSend(
				configs.getMessagingExchange(), // messaging exchange
				configs.getQueueDeliveryReport(), // dlr routing key
				message  // message 
		);
	}
}
