package com.exception;
import org.springframework.security.core.AuthenticationException;
public class TokenFailureException extends AuthenticationException {
	private static final long serialVersionUID = 1L;

	public TokenFailureException(String msg) {
		super(msg);
	}

}
