package com.imalipay.messaging.sms.smpp;

import com.cloudhopper.commons.charset.CharsetUtil;
import com.cloudhopper.smpp.SmppConstants;
import com.cloudhopper.smpp.SmppSession;
import com.cloudhopper.smpp.pdu.SubmitSm;
import com.cloudhopper.smpp.pdu.SubmitSmResp;
import com.cloudhopper.smpp.tlv.Tlv;
import com.cloudhopper.smpp.type.RecoverablePduException;
import com.cloudhopper.smpp.type.SmppChannelException;
import com.cloudhopper.smpp.type.SmppTimeoutException;
import com.cloudhopper.smpp.type.UnrecoverablePduException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SmppSenderClient 
{
	private final SmppSession session;
    private final AddressBuilder addressBuilder;
	
	public void send(String message, String from, String to, int messageTimeOut) 
		throws UnrecoverablePduException, 
				SmppTimeoutException, 
				SmppChannelException, 
				InterruptedException, 
				RecoverablePduException
	{
		SubmitSm submit = new SubmitSm();
		byte[] textBytes = CharsetUtil.encode(message, CharsetUtil.CHARSET_ISO_8859_1);
		// set encoding for sending SMS
		submit.setDataCoding(SmppConstants.DATA_CODING_LATIN1);
		// request for delivery reports
		submit.setRegisteredDelivery(SmppConstants.REGISTERED_DELIVERY_SMSC_RECEIPT_REQUESTED);
		// set the message body
		if (textBytes != null && textBytes.length > 255) {
			submit.addOptionalParameter(new Tlv(SmppConstants.TAG_MESSAGE_PAYLOAD, textBytes, "message_payload"));
		} else {
			submit.setShortMessage(textBytes);
		}

		submit.setSourceAddress(addressBuilder.createSourceAddress(from));
		submit.setDestAddress(addressBuilder.createDestinationAddress(to));
		SubmitSmResp submitResponse = session.submit(submit, messageTimeOut);
		
		int responseCode = submitResponse.getCommandStatus();
		if (responseCode != SmppConstants.STATUS_OK) 
		{
			log.error("error response code {} and message {}", 
					responseCode, submitResponse.getResultMessage());
			throw new IllegalStateException(submitResponse.getResultMessage());
		}
		log.info("SMS submitted, message id {}", submitResponse.getMessageId());
    }
}
