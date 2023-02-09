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
public class BotpressResponseDTO 
{
	private Collection<BotpressResponsePayloadDTO> responses;
}
