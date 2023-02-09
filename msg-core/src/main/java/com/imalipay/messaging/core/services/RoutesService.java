package com.imalipay.messaging.core.services;


import org.springframework.stereotype.Service;

import com.imalipay.messaging.core.models.Routes;
import com.imalipay.messaging.core.repositories.RoutesRepository;
import com.imalipay.messaging.library.dtos.DeliveryMode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class RoutesService 
{
	private RoutesRepository routesRepository;
	
	public Routes findRoute(DeliveryMode deliveryMode, String countryCode) 
		throws Exception
	{
		log.debug("fetching route to forward message");
		return routesRepository.findByDeliveryModeAndCountryCode(
			deliveryMode, countryCode)
			.orElseThrow(()->new Exception(String.format(
					"Route not found for %s in %s",
					deliveryMode.toString(),
					countryCode)));
	}
}
