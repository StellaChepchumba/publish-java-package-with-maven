package com.imalipay.messaging.whatsapp.configs.gupshup;


import com.imalipay.messaging.library.dtos.MessageStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GupshupStatusMapper 
{
	private GupshupStatusMapper() {}
	
	private static final String SUBMITTED = "submitted";
	
	public static MessageStatus map(String gupshupStatus)
	{
		if(gupshupStatus.equals(SUBMITTED))
		{
			return MessageStatus.SENT;
		}
		log.error("unable to map gupshup status {}", gupshupStatus);
		throw new RuntimeException(
				String.format("unable to map status %s", gupshupStatus));
	}
}
