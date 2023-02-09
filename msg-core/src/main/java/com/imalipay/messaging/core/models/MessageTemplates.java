package com.imalipay.messaging.core.models;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.imalipay.messaging.library.dtos.Events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message_templates")
public class MessageTemplates 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer messageTemplateId;
	@Enumerated(EnumType.STRING)
	private Events event;
	private String template;
	private String source;
	
	
	private String countryCode;
	@Enumerated(EnumType.STRING)
	private DeliveryMode deliveryMode;
	private ZonedDateTime createdAt;
}	
