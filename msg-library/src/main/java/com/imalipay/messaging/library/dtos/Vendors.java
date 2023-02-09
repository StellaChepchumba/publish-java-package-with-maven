package com.imalipay.messaging.library.dtos;

import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Vendors 
{
	private String vendorCode;
	private String vendorName;
	private Country country;
	private ZonedDateTime createdAt;
}
