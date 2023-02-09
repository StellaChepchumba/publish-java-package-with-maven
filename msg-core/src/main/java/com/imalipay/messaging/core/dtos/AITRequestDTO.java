package com.imalipay.messaging.core.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AITRequestDTO 
{
	private String sessionId;
	private String serviceCode;
	@NotBlank(message = "Please enter phone number")
	private String phoneNumber;
	private String text;
}
