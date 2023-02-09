package com.imalipay.messaging.library.dtos;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SendNotificationDTO 
{
	@NotBlank(message = "Please enter the message receiver")
	private Map<DeliveryMode, String> to;
	@NotBlank(message = "Please enter the event")
	private Events event;
	@NotNull(message = "Please enter notification data")
	private Map<String , String> data;
	@NotNull(message = "Please enter meta data")
	private Map<String , String> metaData;
}
