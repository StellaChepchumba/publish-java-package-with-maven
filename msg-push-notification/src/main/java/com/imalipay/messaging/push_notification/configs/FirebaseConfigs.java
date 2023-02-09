package com.imalipay.messaging.push_notification.configs;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class FirebaseConfigs 
{
	private final ApplicationConfigs configs;
	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException 
	{
		var file = new ClassPathResource(configs.getCredentialPath());
	    GoogleCredentials googleCredentials = GoogleCredentials
	            .fromStream(file.getInputStream());
	    FirebaseOptions firebaseOptions = FirebaseOptions
	            .builder()
	            .setCredentials(googleCredentials)
	            .build();
	    FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, configs.getFirebaseAppName());
	    return FirebaseMessaging.getInstance(app);
	}
}
