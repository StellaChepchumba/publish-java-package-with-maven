package com.imalipay.messaging.core.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class BotpressRequestDTO 
{
	private String text;
	private String type;
}