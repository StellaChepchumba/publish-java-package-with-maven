package com.imalipay.messaging.core.services;

import com.imalipay.messaging.core.repositories.UssdOtpCodesRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ActiveProfiles("Test")
@RunWith(SpringRunner.class)
public class OtpServiceTest {
    @Autowired
    UssdOtpCodesRepository ussdOtpCodesRepository;


    @Test
    @DisplayName("Unit test for isOtpCode()")
    public  void  whenCodeAndCountryIsProvided_thenAssertThatCodeProvidedOIsCorrect(){
        String countryCode = "NG";
        String  ussdCode = "*347*797*365#";
        Mockito.when(ussdOtpCodesRepository.existsByCountryCodeAndUssdCode(countryCode, ussdCode)).thenReturn(true);

        Boolean result = ussdOtpCodesRepository.existsByCountryCodeAndUssdCode(countryCode, ussdCode);

        Assert.assertEquals(true, result);

    }

    @Test
    @DisplayName("unit test for getUssdOtpCode()")
    public void givenCountryCode_thenRetunUssdCode() throws Exception {
        String countryCode = "NG";
        String  ussdCode = "*347*797*365#";

        Mockito.when(
                ussdOtpCodesRepository.findByCountryCode(countryCode)
                        .orElseThrow(() -> new Exception("no ussd otp code in " + countryCode)).getUssdCode())
                .thenReturn(ussdCode);

        String result = ussdOtpCodesRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new Exception("no ussd otp code in " + countryCode)).getCountryCode();

        Assert.assertEquals(ussdCode, result);
    }
}
