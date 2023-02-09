package com.imalipay.messaging.library.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Country 
{
	public Country(String isoCode)
	{
		this.isoCode = isoCode;
	}
	
	private String isoCode;
	private String name;
}
