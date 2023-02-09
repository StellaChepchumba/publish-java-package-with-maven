package com.imalipay.messaging.core.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GupshupSenderDTO 
{
	private String phone;
	private String name;
	@JsonProperty(value = "country_code")
	private String countryCode;
	@JsonProperty(value = "dial_code")
	private String dialCode;
}
