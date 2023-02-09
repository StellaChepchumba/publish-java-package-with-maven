package com.imalipay.messaging.dlr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class DlrApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(DlrApplication.class, args);
	}
}