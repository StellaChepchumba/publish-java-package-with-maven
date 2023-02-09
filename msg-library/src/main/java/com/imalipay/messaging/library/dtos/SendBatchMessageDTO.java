package com.imalipay.messaging.library.dtos;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;

import javax.validation.constraints.Future;
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
public class SendBatchMessageDTO 
{
	@NotBlank(message = "Please enter the message sender")
	private String from;
	@NotNull(message = "Please enter the message receivers")
	private Collection<String> to;
	@NotBlank(message = "Please enter the message body")
	private String body;
	private Map<String , String> extraData;
	@Future
	private ZonedDateTime scheduledSendTime;
}
