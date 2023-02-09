package com.imalipay.messaging.sms.smpp;

import javax.validation.constraints.NotNull;

import com.cloudhopper.commons.gsm.TypeOfAddress;
import com.cloudhopper.smpp.type.Address;

import static java.util.Objects.requireNonNull;

public class AddressBuilder 
{

	private final TypeOfAddressParser addressParser;

	public AddressBuilder(@NotNull TypeOfAddressParser addressParser) 
	{
		this.addressParser = requireNonNull(addressParser);
	}

	@NotNull
	public Address createSourceAddress(@NotNull String source) 
	{
		return convertToAddress(source, addressParser.getSource(source));
	}

	@NotNull
	public Address createDestinationAddress(@NotNull String msisdn) 
	{
		return convertToAddress(msisdn, addressParser.getDestination(msisdn));
	}

	private Address convertToAddress(String value, TypeOfAddress typeOfAddress) 
	{
		byte ton = (byte) typeOfAddress.getTon().toInt();
		byte npi = (byte) typeOfAddress.getNpi().toInt();
		return new Address(ton, npi, value);
	}
}