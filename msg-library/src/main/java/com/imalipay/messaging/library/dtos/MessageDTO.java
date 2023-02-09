package com.imalipay.messaging.library.dtos;

import java.time.ZonedDateTime;
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
public class MessageDTO 
{
	private UUID messageId;
	private String externalId;
	private String subject;
	private String from;
	private String to;
	private String body;
	private int numberOfSends;
	private int maxNumberOfSends;
	private Map<String, String> extraData;
	private ZonedDateTime sentAt;
	private ZonedDateTime createdAt;
	private MessageStatus status;
	private DeliveryMode deliveryMode;
	private String countryCode;
	private String vendorCode;
	private Integer routeId;
	private String description;
}

