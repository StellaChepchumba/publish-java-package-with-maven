package com.imalipay.messaging.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication
public class MessagingCore 
{
	public static void main(String [] args) 
	{
		SpringApplication.run(MessagingCore.class, args);
	}
}
