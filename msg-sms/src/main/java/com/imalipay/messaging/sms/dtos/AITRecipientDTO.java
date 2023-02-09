package com.imalipay.messaging.sms.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AITRecipientDTO 
{
	private String cost;
	private String messageId;
	private String number;
	private String status;
	private String statusCode;
}
