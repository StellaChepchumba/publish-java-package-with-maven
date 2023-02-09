package com.imalipay.messaging.library.dtos;

import java.util.Collection;
import java.util.Collections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO <E>
{
	private String statusCode;
	private String statusMessage;
	private Collection<E> data;
	private Collection<String> errors;
	
	public ResponseDTO(String statusCode, String statusMessage)
	{
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.data = Collections.emptyList();
		this.errors = Collections.emptyList();
	}
	
	public ResponseDTO(String statusCode, String statusMessage, Collection<E> data)
	{
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.data = data;
		this.errors = Collections.emptyList();
	}
}	
