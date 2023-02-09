package com.imalipay.messaging.core.services;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imalipay.messaging.core.configs.ApplicationConfigs;
import com.imalipay.messaging.core.repositories.UssdOtpCodesRepository;
import com.imalipay.messaging.library.dtos.ResponseDTO;
import com.imalipay.messaging.library.dtos.StatusCodes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class OtpService 
{

    private final UssdOtpCodesRepository ussdOtpCodesRepository;
	private final RestTemplate restTemplate = new RestTemplate();
	private final ApplicationConfigs configs;
	private final ObjectMapper mapper;

	public String getUssdOtpCode(String countryCode) throws Exception 
	{
		var otpCode = ussdOtpCodesRepository.findByCountryCode(countryCode)
			.orElseThrow(() -> new Exception("no ussd otp code in " + countryCode));
		
		return otpCode.getUssdCode();
	}
	
	public boolean isOtpCode(String code, String countryCode)
	{
		return ussdOtpCodesRepository.existsByCountryCodeAndUssdCode(countryCode, code);
	}
	
	public void verifyOtp(String msisdn, String countryCode) throws JsonProcessingException
	{
		var stringResponse = restTemplate.postForObject(
				configs.getOtpVerifyUrl(), 
				new HashMap<String, String>(), 
				String.class,
				countryCode, msisdn);
		
		log.debug("verify Otp response {}", stringResponse);
		var response = mapper.readValue(stringResponse, new TypeReference<ResponseDTO<String>>() {});
				
		if (response != null && response.getStatusCode().equals(StatusCodes.SUCCESS)) 
		{
			log.error("verified otp result: {}", response.getStatusMessage());
			return;
		}
				
		log.error("failed to verify otp");
		throw new RuntimeException("failed to verify otp");
	}
}
