package com.imalipay.messaging.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.imalipay.messaging.core.models.Country;

public interface CountryRepository extends JpaRepository<Country, String>
{

}
