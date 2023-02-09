package com.imalipay.messaging.library.utils;

import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Utils 
{
	
	private Utils() {}
	
	private static final PhoneNumberUtil msisdnUtil = PhoneNumberUtil.getInstance();
	
	public static String msisdnFormate(String msisdn, String countryCode) throws RuntimeException
	{
		try {
			var number = msisdnUtil.parse(msisdn, countryCode);

			log.trace("msisdn formate country code {}, national number {}", number.getCountryCode(), number.getNationalNumber());
			var formattedMsisdn = String.format("%d%d", number.getCountryCode(), number.getNationalNumber());
			log.trace("formatted msisdn {}", formattedMsisdn);
			if (!msisdnUtil.isValidNumber(number)) 
			{
				throw new RuntimeException(String.format("msisdn %s is not valid in %s", msisdn, countryCode));
			}
			return formattedMsisdn;
		} catch (Exception e) {
			log.error("msisdn formate exception: {}", e.getMessage());
			throw new RuntimeException(String.format("msisdn %s is not valid in %s", msisdn, countryCode));
		}
	}
}
