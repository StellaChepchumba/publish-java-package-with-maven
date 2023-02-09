package com.imalipay.messaging.sms.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.type.Address;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UwaziiService 
{

	private final SmppSession session;
	
	public UwaziiService(@Qualifier("uwaziiSmppSession") SmppSession session)
	{
		this.session = session;
	}
	
	public String sendTextMessage(
			String message, 
			String sourceAddress, 
			String destinationAddress
	) throws RecoverablePduException, 
				UnrecoverablePduException, 
				SmppTimeoutException, 
				SmppChannelException,
				InterruptedException 
	{
		log.trace("sending sms message");
		// Check if session is still active
		if (!session.isBound()) {
			log.error("SMPP session is not connected");
			throw new IllegalStateException("SMPP session is not connected");
		}

		SubmitSm submit = new SubmitSm();
		byte[] textBytes = CharsetUtil.encode(message, CharsetUtil.CHARSET_ISO_8859_1);
		// set encoding for sending SMS
		submit.setDataCoding(SmppConstants.DATA_CODING_LATIN1);
		// request for delivery reports
		submit.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
		// set the message body
		if (textBytes != null && textBytes.length > 255) 
		{
			log.trace("message size greater than 255 bytes");
			submit.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
		} else {
			submit.setShortMessage(textBytes);
		}

		submit.setSourceAddress(new Address((byte) 0x05, (byte) 0x01, sourceAddress));
		submit.setDestAddress(new Address((byte) 0x01, (byte) 0x01, destinationAddress));
		// submit message to SMSC for delivery with a timeout of 10000ms
		SubmitSmResp submitResponse = session.submit(submit, 10000); // TODO CONFIFURE TIMEOUT
		int responseCode = submitResponse.getCommandStatus();
		if (responseCode != SmppConstants.STATUS_OK) {
			log.error("error response code {} and message {}", responseCode, submitResponse.getResultMessage());
			throw new IllegalStateException(submitResponse.getResultMessage());
		}
		log.trace("SMS submitted, message id {}", submitResponse.getMessageId());
		return submitResponse.getMessageId();
	}
}
