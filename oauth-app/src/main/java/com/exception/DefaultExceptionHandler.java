package com.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amazonaws.services.apigateway.model.NotFoundException;
import com.models.ExceptionModel;

@ControllerAdvice
public class DefaultExceptionHandler {
    @Autowired
    HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    public @ResponseBody Object handleUncaughtException(Exception ex) {
	ExceptionModel em = new ExceptionModel();
	em.setError("Unknown Error");
	em.setException(ex.getClass().toString());
	em.setStatus("500");
	em.setMessage(ex.getMessage());
	if (ex instanceof BadCredentialsException || ex instanceof InternalAuthenticationServiceException) {
	    em.setError("Request Error");
	    em.setException(ex.getClass().toString());
	    em.setStatus("400");
	    em.setMessage(ex.getMessage());
	    return new ResponseEntity<>(em, HttpStatus.valueOf(Integer.parseInt("400")));
	}
	if (ex instanceof ResourceNotFoundException||ex instanceof NotFoundException) {
	    em.setError("Request Not Found");
	    em.setException(ex.getClass().toString());
	    em.setStatus("404");
	    em.setMessage(ex.getMessage());
	    return new ResponseEntity<>(em, HttpStatus.valueOf(Integer.parseInt("404")));
	}
	if (ex instanceof PermissionDeniedException) {
	    em.setError("forbidden");
	    em.setException(ex.getClass().toString());
	    em.setStatus("403");
	    em.setMessage(ex.getMessage());
	    return new ResponseEntity<>(em, HttpStatus.valueOf(Integer.parseInt("403")));
	}
	if (ex instanceof UnauthorizedUserException) {
	    em.setError("Unauthorized");
	    em.setException(ex.getClass().toString());
	    em.setStatus("401");
	    em.setMessage(ex.getMessage());
	    return new ResponseEntity<>(em, HttpStatus.valueOf(Integer.parseInt("401")));
	}

	return new ResponseEntity<>(em, HttpStatus.valueOf(Integer.parseInt("500")));
    }
}
