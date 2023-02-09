package com.imalipay.messaging.sms.smpp;

import java.util.Collection;
import java.util.function.Consumer;

import org.joda.time.DateTimeZone;

import com.cloudhopper.smpp.impl.DefaultSmppSessionHandler;
import com.cloudhopper.smpp.pdu.DeliverSm;
import com.cloudhopper.smpp.pdu.PduRequest;
import com.cloudhopper.smpp.pdu.PduResponse;
import com.cloudhopper.smpp.util.DeliveryReceiptException;
import com.imalipay.messaging.library.smpp.DeliveryReceipt;
import com.imalipay.messaging.sms.dtos.smpp.DeliveryReport;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("rawtypes")
@AllArgsConstructor
public class DlrResponseHandler extends DefaultSmppSessionHandler 
{
	private final String clientId;
	private final Collection<Consumer<DeliveryReport>> dlrHandlers;

	@Override
	public PduResponse firePduRequestReceived(PduRequest request) 
	{
		log.trace("raw pdu request received {}", request);
		if (isDelivery(request)) 
		{
			DeliverSm dlr = (DeliverSm) request;
			try {
	            DeliveryReport report = toReport(dlr);
	            log.debug("deliery report {}",report);
	            // apply consumers to dlr reports
	            dlrHandlers.stream().forEach(consumer->consumer.accept(report));
				
	        } catch (DeliveryReceiptException e) {
	        	log.error("Error while handling delivery", e);
	        }
        }
		return request.createResponse();
	}
	
	private DeliveryReport toReport(DeliverSm deliverSm) throws DeliveryReceiptException 
	{
        byte[] shortMessage = deliverSm.getShortMessage();
        String sms = new String(shortMessage);
        DeliveryReceipt deliveryReceipt = DeliveryReceipt.parseShortMessage(sms, DateTimeZone.UTC);
        return DeliveryReport.of(deliveryReceipt, clientId);
    }
	
	private boolean isDelivery(PduRequest pduRequest) 
	{
        return pduRequest.isRequest() && pduRequest.getClass() == DeliverSm.class;
    }
	
}
