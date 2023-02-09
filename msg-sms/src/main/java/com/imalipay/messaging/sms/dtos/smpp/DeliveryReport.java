package com.imalipay.messaging.sms.dtos.smpp;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.springframework.lang.Nullable;

import com.cloudhopper.smpp.util.DeliveryReceipt;

import lombok.Data;

@Data
public class DeliveryReport 
{

    private String messageId;
    private ZonedDateTime deliveryDate;
    private ZonedDateTime submitDate;
    private int deliveredCount;
    private int submitCount;
    private int errorCode;
    private int state;
    private String responseClientId;
    private String text;

    public static DeliveryReport of(
    	@NotNull final DeliveryReceipt deliveryReceipt, 
    	@NotNull String responseClientId)
    {
        DeliveryReport deliveryReport = new DeliveryReport();
        deliveryReport.setDeliveredCount(deliveryReceipt.getDeliveredCount());
        deliveryReport.setMessageId(deliveryReceipt.getMessageId());
        deliveryReport.setErrorCode(deliveryReceipt.getErrorCode());
        deliveryReport.setState(deliveryReceipt.getState());
        deliveryReport.setSubmitCount(deliveryReceipt.getSubmitCount());
        deliveryReport.setDeliveryDate(convert(deliveryReceipt.getDoneDate()));
        deliveryReport.setSubmitDate(convert(deliveryReceipt.getSubmitDate()));
        deliveryReport.setResponseClientId(responseClientId);
        deliveryReport.setText(deliveryReceipt.getText());
        return deliveryReport;
    }
    
    @Nullable
    public static ZonedDateTime convert(@Nullable DateTime dateTime) 
    {
        if (dateTime == null) 
        {
            return null;
        }

        Instant instant = Instant.ofEpochMilli(dateTime.toInstant().getMillis());
        ZoneId zoneId = ZoneId.of(dateTime.getZone().getID());
        return instant.atZone(zoneId);
    }
}