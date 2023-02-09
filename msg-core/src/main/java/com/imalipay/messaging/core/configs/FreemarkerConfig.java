package com.imalipay.messaging.core.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.imalipay.messaging.core.models.MessageTemplates;
import com.imalipay.messaging.core.repositories.MessageTemplatesRepository;

import freemarker.cache.StringTemplateLoader;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class FreemarkerConfig 
{

	private final MessageTemplatesRepository tempRepo;
	
	@Primary
	@Bean   
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration() 
	{
	    // Create new configuration bean
	    FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
	    // Create template loader
	    StringTemplateLoader sTempLoader = new StringTemplateLoader();
	    // Find all templates
	    Iterable<MessageTemplates> ite = tempRepo.findAll();
	    // Load all templates
	    ite.forEach(template -> 
	        // Loanding individual template
	        sTempLoader.putTemplate(
	        	template.getMessageTemplateId().toString(), 
	        	template.getTemplate())
	    );
	    // Set loader
	    bean.setPreTemplateLoaders(sTempLoader);
	    return bean;
	}
}
