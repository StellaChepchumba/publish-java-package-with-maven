package com.imalipay.messaging.core.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GupshupDTO 
{
	private String app;
	private String timestamp;
	private String version;
	private String type;
	private GupshupPayloadDTO payload;
	private GupshupContextDTO context;
}
