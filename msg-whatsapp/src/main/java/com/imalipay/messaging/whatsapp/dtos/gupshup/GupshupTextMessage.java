package com.imalipay.messaging.whatsapp.dtos.gupshup;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GupshupTextMessage implements GupshupMessage
{
	private final String channel = "whatsapp";
	@NotBlank(message = "please enter the gupshup destination msisdn")
	private String destination;
	@NotBlank(message = "please enter the gupshup source msisdn")
	private String source;
	@NotBlank(message = "please enter the gupshup text message")
	private String message;
}
