package com.imalipay.messaging.core.dtos;

import java.util.Map;

import com.imalipay.messaging.core.configs.GupshupMessageTypes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GupshupPayloadDTO 
{
	private String id;
	private String source;
	private GupshupMessageTypes type;
	private String app;
	private Map<String, String> payload;
	private GupshupSenderDTO sender;
}
