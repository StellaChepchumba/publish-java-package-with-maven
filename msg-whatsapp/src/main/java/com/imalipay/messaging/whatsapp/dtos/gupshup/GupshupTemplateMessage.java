package com.imalipay.messaging.whatsapp.dtos.gupshup;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Getter
@Setter
@Builder
@ToString
public class GupshupTemplateMessage implements GupshupMessage
{
	private Template template;
	private String source;
	private String destination;
	
	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	public class Template
	{
		private String id;
		private String[] params;
	}
}
