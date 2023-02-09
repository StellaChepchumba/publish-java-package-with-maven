package com.imalipay.messaging.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.imalipay.messaging.core.models.MessageTemplates;
import com.imalipay.messaging.library.dtos.DeliveryMode;
import com.imalipay.messaging.library.dtos.Events;

public interface MessageTemplatesRepository extends JpaRepository<MessageTemplates, Integer>,
JpaSpecificationExecutor<MessageTemplates>
{

	Optional<MessageTemplates> findByEventAndDeliveryModeAndCountryCode(
			Events event, 
			DeliveryMode deliveryMode,
			String countryCode);
 
}
