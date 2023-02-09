package com.imalipay.messaging.core.models;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.imalipay.messaging.library.dtos.MessageStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="messages")
public class Messages 
{
	@Id
    @Column(updatable = false, nullable = false)
	private UUID messageId;
	private String source;
	private String recipient;
	private String body;
	private ZonedDateTime sentAt;
	@Builder.Default
	@ColumnDefault("0")
	private Integer numberOfSends = 0;
	@Builder.Default
	@ColumnDefault("5")
	private Integer maxNumberOfSends = 5;
	private String description;
	@ManyToOne
	@JoinColumn(name = "route_id")
	private Routes route;
	@Enumerated(EnumType.STRING)
	private MessageStatus status;
	private String countryCode;
	@CreationTimestamp
	private ZonedDateTime createdAt;
	@UpdateTimestamp
	private ZonedDateTime updatedAt;
}
