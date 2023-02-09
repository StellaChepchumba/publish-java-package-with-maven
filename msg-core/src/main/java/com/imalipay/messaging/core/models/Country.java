package com.imalipay.messaging.core.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="countries")
public class Country 
{
	public Country(String isoCode)
	{
		this.isoCode = isoCode;
	}
	
	@Id
	private String isoCode;
	private String name;
}
