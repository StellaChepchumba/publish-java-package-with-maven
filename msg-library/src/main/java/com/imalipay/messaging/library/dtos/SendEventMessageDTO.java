package com.imalipay.messaging.library.dtos;

import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SendEventMessageDTO 
{
	@NotBlank(message = "Please enter the message receiver")
	private String to;
	@NotNull(message = "Please enter extra data")
	private Map<String , String> extraData;
}
