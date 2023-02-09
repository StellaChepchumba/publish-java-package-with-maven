package com.imalipay.messaging.whatsapp.dtos.gupshup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GupshupLocationMessage implements GupshupMessage
{
	private final String channel = "whatsapp";
	@NotBlank(message = "please enter the gupshup destination msisdn")
	private String destination;
	@NotBlank(message = "please enter the gupshup source msisdn")
	private String source;
	@NotNull(message = "please enter the gupshup message")
	private Message message;
	
	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	public class Message 
	{
		private final String type = GupshupMessageTypes.LOCATION.toString().toLowerCase();
		private double longitude;
		private double latitude;
		private String name;
		private String address;
	}
}
