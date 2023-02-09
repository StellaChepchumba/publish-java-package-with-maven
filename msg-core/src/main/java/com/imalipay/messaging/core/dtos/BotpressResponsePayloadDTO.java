package com.imalipay.messaging.core.dtos;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BotpressResponsePayloadDTO 
{
	private String type;
	private String text;
	private String image;
	private Collection<ChoiceDTO> choices;
}

