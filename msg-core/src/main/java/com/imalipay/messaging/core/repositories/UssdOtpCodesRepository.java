package com.imalipay.messaging.core.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.imalipay.messaging.core.models.UssdOtpCodes;


public interface UssdOtpCodesRepository extends JpaRepository<UssdOtpCodes, UUID>, 
JpaSpecificationExecutor<UssdOtpCodes>
{
	public Optional<UssdOtpCodes> findByCountryCode(String countryCode);
	public boolean existsByCountryCodeAndUssdCode(String countryCode, String ussdCode);
}
