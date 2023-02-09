package com.imalipay.messaging.push_notification.services;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.*;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.imalipay.messaging.library.dtos.MessageDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class FirebaseService 
{
	private final FirebaseMessaging firebaseMessaging;

    public String sendNotification(MessageDTO message) throws FirebaseMessagingException 
    {
    	log.debug("sending out push notification {}", message);

    	// notification preview 
    	var title = message.getFrom();
    	if (message.getExtraData() != null && message.getExtraData().containsKey("subject")) 
    	{
			title = message.getExtraData().get("subject");
		}
    	
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(message.getBody())
                .build();
    	log.debug("notification {}", notification);

        return firebaseMessaging.send(Message.builder()
                .setToken(message.getTo())
                .setNotification(notification)
                .putAllData(message.getExtraData())
                .build());
    }
}
