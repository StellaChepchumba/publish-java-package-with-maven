package com.imalipay.messaging.library.dtos;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailDTO 
{
	@NotBlank(message = "Please enter email subject")
	private String subject;
	@NotBlank(message = "Please enter email body")
	private String body;
	@Valid
//	@NotBlank(message = "Please enter the destination email address")
	private Collection<EmailDTO> to;
	private Collection<EmailDTO> cc;
	private Collection<EmailDTO> bcc;
	private Collection<AttachmentDTO> attachments;
	@Builder.Default
	private Map<String , String> metaData = new HashMap<>();
	@Future
	private ZonedDateTime scheduledSendTime;
	private String description;
}
