package com.imalipay.messaging.core.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.imalipay.messaging.library.dtos.ResponseDTO;
import com.imalipay.messaging.library.dtos.StatusCodes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Order( value = Ordered.HIGHEST_PRECEDENCE )
public class WebExceptionHandler extends ResponseEntityExceptionHandler 
{
	@Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        NoHandlerFoundException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) 
    {
    	log.error("no handler found exception being handled");
    	
    	// status message for this error
    	StringBuilder statusMessageBuilder = new StringBuilder();
    	statusMessageBuilder.append(status.getReasonPhrase()).append(" ");
    	statusMessageBuilder.append(request.getDescription(false));
    	
    	log.error("no handler found status message: {} ", 
    			statusMessageBuilder);
    	
        // build the response payload
    	ResponseDTO<String> errorResponse = new ResponseDTO<>(
    			StatusCodes.FAILED,
    			"URL does not exist");
        
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
	
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatus status,
        WebRequest request
    ) 
    {
    	log.error("method argument not valid exception being handled");
    	
    	// status message for this error
    	List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) 
        {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) 
        {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        
        String statusMessage = "method validation failing check required parameters";
    	log.error(statusMessage);
        log.error(ex.getMessage(), ex);
    	
        // build the response payload
        ResponseDTO<String> errorResponse = new ResponseDTO<>(
    			StatusCodes.FAILED,
    			"validation failing check required parameters",
    			Arrays.asList(),
    			errors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
    
    @ExceptionHandler
    public final ResponseEntity<Object> handleGeneralExceptions(
		Exception ex,
        WebRequest request
    )
    {
    	log.error("default exceptions being handled");
    	log.trace(ex.getMessage());
    	log.trace(ex.getMessage(), ex);
    	
    	// status message for this error
        String statusMessage = ex.getMessage();
        
    	log.error(statusMessage);
    	
    	// build the response payload
        ResponseDTO<String> errorResponse = new ResponseDTO<>(
    			StatusCodes.FAILED,
    			"error processing your request",
    			Arrays.asList(),
    			Arrays.asList(statusMessage));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.OK);
    }
}