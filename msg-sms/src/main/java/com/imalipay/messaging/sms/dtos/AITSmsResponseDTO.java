package com.imalipay.messaging.sms.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AITSmsResponseDTO 
{
	@JsonProperty("SMSMessageData")
	private AITSmsMessageDataDTO smsMessageData;
}
