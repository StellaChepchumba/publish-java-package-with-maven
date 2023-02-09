package com.imalipay.messaging.email.services;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.imalipay.messaging.email.utils.FileUtils;
import com.imalipay.messaging.library.dtos.AttachmentDTO;
import com.imalipay.messaging.library.dtos.EmailDTO;
import com.imalipay.messaging.library.dtos.EmailMessageDTO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class GmailService 
{
    private final JavaMailSender emailSender;
    
	public void sendEmailMessage(EmailMessageDTO message) throws Exception
	{
		log.debug("sending out email {}", message);
		
		try {
			MimeMessage mail = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			
			helper.setFrom(message.getFrom());
			helper.setSubject(message.getSubject());
			helper.setText(message.getBody());
			helper.setTo(mapper(message.getTo()));
			helper.setCc(mapper(message.getCc()));
			helper.setBcc(mapper(message.getBcc()));
			attacheFiles(helper, message.getAttachments());
			
			log.trace("email to be sent {}", mail);
			
	        emailSender.send(mail);
	        
	        log.trace("email sent");
	    }
		catch (MessagingException | IOException e) {
			log.error("failed to send email {}", e.getMessage());
	    	throw new Exception(e);
	    }
	}
	
	private InternetAddress[] mapper(Collection<EmailDTO> emails)
	{
		log.trace("mapping email collection to InternetAddress array");
		
		var internetAddresses = emails.stream()
				.map(emailDTO -> {
					try {
						return new InternetAddress(emailDTO.getEmail(), emailDTO.getName());
					} catch (UnsupportedEncodingException e) {
						log.error("failed to map email {} skipping", emailDTO.getEmail());
					}
					return null;
				})
				.filter(address -> address != null)
				.collect(Collectors.toList())
				.toArray(InternetAddress[]::new);
		
		log.trace("array of internet address {}", internetAddresses.toString());
		
		return internetAddresses;
	}
	
	private void attacheFiles (
		MimeMessageHelper helper, 
		Collection<AttachmentDTO> attachements
	) throws MessagingException, IOException
	{
		log.trace("attaching files to email");
		attachements.stream()
			.forEach(file -> {
				try {
					attacheFile(helper, file);
				} catch (MessagingException | IOException e) {
					log.error("failed to attach file {}, {}", file.getName(), e.getMessage());
				}
			});
	} 
	
	private void attacheFile (
		MimeMessageHelper helper, 
		AttachmentDTO attachement
	) throws MessagingException, IOException
	{
		var url = new URL(attachement.getUrl());
		var file = FileUtils.downloadFromUrl(url);
		
		helper.addAttachment(attachement.getName(), file);
	}
}
