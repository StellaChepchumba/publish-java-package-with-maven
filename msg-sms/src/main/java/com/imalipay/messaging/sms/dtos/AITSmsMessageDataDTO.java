package com.imalipay.messaging.sms.dtos;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AITSmsMessageDataDTO 
{
	@JsonProperty("Message")
	private String message;
	@JsonProperty("Recipients")
	private Collection<AITRecipientDTO> recipients;
}
