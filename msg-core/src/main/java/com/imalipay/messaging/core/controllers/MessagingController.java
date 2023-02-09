package com.imalipay.messaging.core.controllers;

import java.time.ZonedDateTime;
import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imalipay.messaging.core.models.Messages;
import com.imalipay.messaging.core.models.Routes;
import com.imalipay.messaging.core.models.Vendors;
import com.imalipay.messaging.core.repositories.specifications.ZonedDateTimeRange;
import com.imalipay.messaging.core.services.MessagingService;
import com.imalipay.messaging.core.services.OtpService;
import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.imalipay.messaging.library.dtos.Events;
import com.imalipay.messaging.library.dtos.MessageDTO;
import com.imalipay.messaging.library.dtos.MessageStatus;
import com.imalipay.messaging.library.dtos.MessageType;
import com.imalipay.messaging.library.dtos.ResponseDTO;
import com.imalipay.messaging.library.dtos.SendBatchMessageDTO;
import com.imalipay.messaging.library.dtos.SendEmailDTO;
import com.imalipay.messaging.library.dtos.SendEventMessageDTO;
import com.imalipay.messaging.library.dtos.SendMessageDTO;
import com.imalipay.messaging.library.dtos.SendNotificationDTO;
import com.imalipay.messaging.library.dtos.StatusCodes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class MessagingController 
{
	// endpoints 
	private static final String SEND_EMAIL = "/{countryCode}/send/delivery-mode/email";
	private static final String SEND_MESSAGE = "/{countryCode}/send/delivery-mode/{deliveryMode}";
	private static final String SEND_MESSAGE_BATCH = "/{countryCode}/send/batch/delivery-mode/{deliveryMode}";
	private static final String SEND_EVENT_NOTIFICATION = "/{countryCode}/send/notification";
	private static final String SEND_EVENT_MESSAGE = "/{countryCode}/send/event/{event}/delivery-mode/{deliveryMode}";
	private static final String SEARCH_MESSAGES = "/{countryCode}/search";
	private static final String GET_DELIVERY_MODES = "/{countryCode}/delivery-mode";
    private static final String USSD_OTP_CODE = "/{countryCode}/messaging/otp/ussd";
	
	private final MessagingService messagingService;
	private final OtpService otpService;
	
	
	@PostMapping(value = SEND_EMAIL)
	public ResponseDTO<String> sendEmail(
		@Valid @RequestBody SendEmailDTO message,
		@PathVariable(required = true) String countryCode) throws Exception
	{
		log.debug("received email to send in {}", message, countryCode);
		
		messagingService.logEmailMessage(
				message, 
				countryCode.toUpperCase());
		
		return new ResponseDTO<>(
				StatusCodes.SUCCESS, 
				"email is being processed");
	}
	
	@PostMapping(value = SEND_MESSAGE)
	public ResponseDTO<String> sendMessage(
		@Valid @RequestBody SendMessageDTO message, 
		@PathVariable(required = true) String deliveryMode,
		@PathVariable(required = true) String countryCode) throws Exception
	{
		log.debug("received {} to send in {}", message, countryCode);
		
		messagingService.logMessage(
				message, 
				DeliveryMode.valueOf(deliveryMode.toUpperCase()), 
				countryCode.toUpperCase());
		
		return new ResponseDTO<>(
				StatusCodes.SUCCESS, 
				"message is being processed");
	}
	
	@PostMapping(value = SEND_MESSAGE_BATCH)
	public ResponseDTO<String> sendBatchMessages(
		@Valid @RequestBody SendBatchMessageDTO message, 
		@PathVariable(required = true) String deliveryMode,
		@PathVariable(required = true) String countryCode) throws Exception
	{
		log.debug("received {} to send in {}", message, countryCode);
		
		messagingService.logBatchMessage(
				message, 
				DeliveryMode.valueOf(deliveryMode.toUpperCase()), 
				countryCode.toUpperCase());
		return new ResponseDTO<>(
				StatusCodes.SUCCESS, 
				"message being processed");
	}
	
	@PostMapping(value = SEND_EVENT_NOTIFICATION)
	public ResponseDTO<String> sendEventNotification(
		@Valid @RequestBody SendNotificationDTO message,
		@PathVariable(required = true) String countryCode) throws Exception
	{
		log.debug("received {} to send noitification in {}", message, countryCode);
		
		messagingService.logEventNotificationMessage(
				message,
				countryCode.toUpperCase());
		
		return new ResponseDTO<>(
				StatusCodes.SUCCESS, 
				"notification is being processed");
	}
	
	@PostMapping(value = SEND_EVENT_MESSAGE)
	public ResponseDTO<String> sendEventMessage(
		@Valid @RequestBody SendEventMessageDTO message, 
		@PathVariable(required = true) String event,
		@PathVariable(required = true) String deliveryMode,
		@PathVariable(required = true) String countryCode) throws Exception
	{
		log.debug("received {} {} to send in {}", event, message, countryCode);
		
		messagingService.logEventMessage(
				message, 
				Events.valueOf(event.toUpperCase()),
				DeliveryMode.valueOf(deliveryMode.toUpperCase()), 
				countryCode.toUpperCase());
		
		return new ResponseDTO<>(
				StatusCodes.SUCCESS, 
				"message is being processed");
	}
	
	@GetMapping(value = GET_DELIVERY_MODES)
	public ResponseDTO<DeliveryMode> getDeliveryModes(
		@PathVariable(required = true) String countryCode)
	{
		return new ResponseDTO<>(
				StatusCodes.SUCCESS, 
				"message is being processed",
				Arrays.asList(DeliveryMode.values()));
	}

	@GetMapping(value = SEARCH_MESSAGES)
	public Page<MessageDTO> searechMessages(
		Pageable page,
		@RequestParam(required = false) String to, 
		@RequestParam(required = false) String from,
		@RequestParam(required = false) DeliveryMode deliveryMode,
		@RequestParam(required = false) MessageType messageType,
		@RequestParam(required = false) MessageStatus messageStatus,
		@RequestParam(required = false) String vendorCode,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDateTime,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDateTime,
		@PathVariable(required = true) String countryCode)
	{
		log.info("received request search for messages");
		log.trace("pagable: {}", page);
		
		var exampleMessage = Messages.builder()
				.recipient(to)
				.source(from)
				.numberOfSends(null)
				.maxNumberOfSends(null)
				.status(messageStatus)
				.route(Routes.builder()
					.deliveryMode(deliveryMode)
					.vendor(Vendors.builder().vendorCode(vendorCode).build())
					.build())
				.build();
		
		return messagingService.search(
			page,
			exampleMessage,
			new ZonedDateTimeRange("createdAt", fromDateTime, toDateTime),
			countryCode.toUpperCase()
		).map(entity -> MessageDTO.builder()
				.body(entity.getBody())
				.countryCode(entity.getCountryCode())
				.deliveryMode(entity.getRoute().getDeliveryMode())
				.from(entity.getSource())
				.to(entity.getRecipient())
				.messageId(entity.getMessageId())
				.vendorCode(entity.getRoute().getVendor().getVendorCode())
				.sentAt(entity.getSentAt())
				.createdAt(entity.getCreatedAt())
				.build());
	}
    
    
    @GetMapping(value = USSD_OTP_CODE)
    public ResponseDTO<String> getUssdOtpCode(
    	@PathVariable(required = true) String countryCode
    ) throws Exception {
        log.trace("get OTP USSD codes in {}", countryCode);

        var code = otpService.getUssdOtpCode(countryCode.toUpperCase());
        
        return new ResponseDTO<>(
                StatusCodes.SUCCESS,
                String.format("USSD OTP code in %s", countryCode.toUpperCase()),
                Arrays.asList(code));
        
    }
}
