package com.imalipay.messaging.sms.smpp;

import org.springframework.scheduling.annotation.Scheduled;

import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.EnquireLink;
import com.cloudhopper.smpp.pdu.EnquireLinkResp;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SmppSessionManager 
{
	public final int sessionTimeout;
	public final SmppSession session;
	public static final String INIATAL_DELAY_MILLI_SECONDS = "30000";
	public static final String FIXED_DELAY_MILLI_SECONDS = "30000";
	
	@Scheduled(
		initialDelayString = INIATAL_DELAY_MILLI_SECONDS, 
		fixedDelayString = FIXED_DELAY_MILLI_SECONDS)
	public void enquireLinkJob() 
	{
		if (session.isBound()) 
		{
			try {
				log.trace("sending enquire_link");
				EnquireLinkResp enquireLinkResp = session.enquireLink(new EnquireLink(), sessionTimeout);
				log.trace("enquire_link_resp: {}", enquireLinkResp);
			} catch (SmppTimeoutException | SmppChannelException e) {
				log.warn("Enquire link failed, executing reconnect; " + e);
				log.error("", e);
			}  catch (InterruptedException e) {
				log.warn("Enquire link interrupted, probably killed by reconnecting");
				log.error("", e);
			} catch (Exception e) {
				log.error("Enquire link failed, executing reconnect", e);
			}
		} else {
			log.error("enquire link running while session is not connected");
		}
	}
}
