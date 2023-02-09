package com.imalipay.messaging.sms.configs.smpp;

import java.util.Arrays;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppClient;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.cloudhopper.smpp.impl.DefaultSmppClient;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;
import com.imalipay.messaging.sms.smpp.AddressBuilder;
import com.imalipay.messaging.sms.smpp.DefaultTypeOfAddressParser;
import com.imalipay.messaging.sms.smpp.DlrResponseHandler;
import com.imalipay.messaging.sms.smpp.SmppSenderClient;
import com.imalipay.messaging.sms.smpp.SmppSessionManager;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "imalipay.messaging.sms.config.smpp.routesms") 
public class RouteSmsConfigs 
{
	private String name;
	private String host;
	private int port;
	private String systemId;
	private String password;
	private int sessionTimeout;
	private boolean logByte;
	private boolean logPdu;
	private int connectionCount;
	private int windowSize;
	
	
	@Bean(name="routesmsSmppSession", destroyMethod = "destroy")
	public SmppSession session(
		@Qualifier("routesmsSmppSessionConfigs") SmppSessionConfiguration configs
	)
		throws SmppTimeoutException, 
				SmppChannelException, 
				UnrecoverablePduException, 
				InterruptedException 
	{
		SmppClient smppClient = new DefaultSmppClient(
				Executors.newCachedThreadPool(), 
				connectionCount); 
		
		return smppClient.bind(
				configs, 
				new DlrResponseHandler(name, Arrays.asList()));
	}
	
	@Bean(name="routesmsSmppSessionConfigs")
	public SmppSessionConfiguration sessionConfiguration() 
	{
		SmppSessionConfiguration sessionConfig = new SmppSessionConfiguration();
		sessionConfig.setName(name);
		sessionConfig.setInterfaceVersion(SmppConstants.VERSION_3_4);
		sessionConfig.setType(SmppBindType.TRANSCEIVER);
		sessionConfig.setHost(host);
		sessionConfig.setPort(port);
		sessionConfig.setSystemId(systemId);
		sessionConfig.setPassword(password);
		sessionConfig.setSystemType(null);
		sessionConfig.getLoggingOptions().setLogBytes(logByte);
		sessionConfig.getLoggingOptions().setLogPdu(logPdu);
		sessionConfig.setWindowSize(windowSize);

		return sessionConfig;
	}
	
	@Bean(name="routesmsSmppSessionManger")
	public SmppSessionManager sessionManager(
		@Qualifier("routesmsSmppSession") SmppSession session
	)
	{
		return new SmppSessionManager(sessionTimeout, session);
	}
	
	@Bean(name = "routesmsSmppClient")
	public SmppSenderClient smppSenderClient(
		@Qualifier("routesmsSmppSession") SmppSession session
	)
	{
		return new SmppSenderClient(
				session, 
				new AddressBuilder(new DefaultTypeOfAddressParser()));
	}
}
