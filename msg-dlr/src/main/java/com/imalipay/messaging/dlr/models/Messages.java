package com.imalipay.messaging.dlr.models;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.imalipay.messaging.library.dtos.MessageStatus;

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
@Table(name="messages")
public class Messages 
{
	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false)
	private UUID messageId;
	private String externalId;
	private String source;
	private String recipient;
	private String body;
	private ZonedDateTime sentAt;
	private int numberOfSends;
	private int maxNumberOfSends;
	private String description;
	private Integer routeId;
	@Enumerated(EnumType.STRING)
	private MessageStatus status;
	private String countryCode;
	@CreationTimestamp
	private ZonedDateTime createdAt;
	@UpdateTimestamp
	private ZonedDateTime updatedAt;
}
