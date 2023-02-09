package com.imalipay.messaging.core.models;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;


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
public class Vendors 
{
	@Id
	private String vendorCode;
	private String vendorName;

	private ZonedDateTime createdAt;
}
