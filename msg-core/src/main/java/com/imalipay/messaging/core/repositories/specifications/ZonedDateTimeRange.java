package com.imalipay.messaging.core.repositories.specifications;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZonedDateTimeRange 
{
	private String field;
    private ZonedDateTime from;
    private ZonedDateTime to;
    private Boolean includeNull;


    public ZonedDateTimeRange(
    	String field, 
    	ZonedDateTime from, 
    	ZonedDateTime to
    )
    {
        this.field = field;
        this.from = from;
        this.to = to;
    }
    
    public ZonedDateTimeRange(
    	String field, 
    	ZonedDateTime from, 
    	ZonedDateTime to, 
    	Boolean includeNull
    ) 
    {
        this.field = field;
        this.from = from;
        this.to = to;
        this.includeNull = includeNull;
    }
    
    public ZonedDateTimeRange(ZonedDateTimeRange other) 
    {
        this.field = other.getField();
        this.from = other.getFrom();
        this.to = other.getTo();
        this.includeNull = other.getIncludeNull();
    }
    public ZonedDateTime getTo() 
    {
        return to;
    }
    
    public ZonedDateTime getFrom() 
    {
        return from;
    }

    public boolean isFromSet() 
    {
        return getFrom() != null;
    }


    public boolean isToSet() 
    {
        return getTo() != null;
    }
    
    public boolean isIncludeNullSet() 
    {
        return includeNull != null;
    }

    public boolean isBetween() 
    {
        return isFromSet() && isToSet();
    }

    public boolean isSet() 
    {
        return isFromSet() || isToSet() || isIncludeNullSet();
    }

    public boolean isValid() 
    {
        if (isBetween()) 
        {
            return getFrom().compareTo(getTo()) <= 0;
        }
        return true;
    }
}
