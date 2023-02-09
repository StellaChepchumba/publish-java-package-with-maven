package com.imalipay.messaging.push_notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class PushNotificationApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(PushNotificationApplication.class, args);
	}
}