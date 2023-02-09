package com.imalipay.messaging.library.dtos;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

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
public class EmailMessageDTO
{
	private UUID messageId;
	private String externalId;
	private String subject;
	private String body;
	private String from;
	private Collection<EmailDTO> to;
	private Collection<EmailDTO> cc;
	private Collection<EmailDTO> bcc;
	private Collection<AttachmentDTO> attachments;
	private int numberOfSends;
	private int maxNumberOfSends;
	private Map<String, String> metaData;
	private ZonedDateTime sentAt;
	private ZonedDateTime createdAt;
	private MessageStatus status;
	@Builder.Default
	private DeliveryMode deliveryMode = DeliveryMode.EMAIL;
	private String countryCode;
	private String vendorCode;
	private Integer routeId;
	private String description;
}
