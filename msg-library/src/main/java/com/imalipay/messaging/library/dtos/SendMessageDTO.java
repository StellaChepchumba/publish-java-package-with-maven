package com.imalipay.messaging.library.dtos;


import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SendMessageDTO 
{
	@NotBlank(message = "Please enter the message sender")
	private String from;
	@NotBlank(message = "Please enter the message receiver")
	private String to;
	@NotBlank(message = "Please enter the message body")
	private String body;
	@Builder.Default
	private Map<String , String> extraData = new HashMap<>();
	@Future
	private ZonedDateTime scheduledSendTime;
}
