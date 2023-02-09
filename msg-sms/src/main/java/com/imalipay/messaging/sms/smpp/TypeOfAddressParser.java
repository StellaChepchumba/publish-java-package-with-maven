package com.imalipay.messaging.sms.smpp;

import javax.validation.constraints.NotNull;

import com.cloudhopper.commons.gsm.TypeOfAddress;

public interface TypeOfAddressParser 
{
    /**
     * Detect ton and npi parameters for message source address
     *
     * @param source message source
     * @return ton and npi for message source
     */
    @NotNull TypeOfAddress getSource(@NotNull String source);

    /**
     * Detect ton and npi parameters for message destination address
     *
     * @param destination message destination
     * @return ton and npi for message destination
     */
    @NotNull TypeOfAddress getDestination(@NotNull String destination);

}