package com.imalipay.messaging.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imalipay.messaging.core.models.Routes;
import com.imalipay.messaging.library.dtos.DeliveryMode;

public interface RoutesRepository extends JpaRepository<Routes, Integer>
{

	Optional<Routes> findByDeliveryModeAndCountryCode(
		DeliveryMode deliveryMode,
		String coutryCode);
	
}
