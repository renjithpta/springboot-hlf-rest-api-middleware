package com.ibs.fixmtoacris.controller;
import javax.servlet.http.HttpServletRequest;

import com.ibs.fixmtoacris.exception.ResourceNotFoundException;
import com.ibs.fixmtoacris.model.ExceptionResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.hyperledger.fabric.gateway.ContractException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ExceptionResponse handleResourceNotFound(final ResourceNotFoundException exception,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse();
		error.setExceptionMessage(exception.getMessage());
		error.callerURL(request.getRequestURI());

		return error;
	}

    @ExceptionHandler(ContractException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ExceptionResponse handleResourceNotFound(final ContractException exception,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse();

		error.setExceptionMessage(exception.getMessage());
		error.callerURL(request.getRequestURI());

		return error;
	}

    

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ExceptionResponse handleException(final Exception exception,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse();
        String message = exception.getMessage();
        if(message.indexOf("does not exits") > 0) {

            message = message.substring(message.indexOf("Error:"), message.indexOf("exits") + 5) ;

        }
		error.setExceptionMessage(exception.getMessage());
		error.callerURL(request.getRequestURI());

		return error;
	}

}