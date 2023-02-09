package com.imalipay.messaging.library.dtos;

import java.time.ZonedDateTime;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Routes 
{
	private Integer routeId;
	private Vendors vendor;
	private Country country;
	private DeliveryMode deliveryMode;
	private ZonedDateTime createdAt;
}
