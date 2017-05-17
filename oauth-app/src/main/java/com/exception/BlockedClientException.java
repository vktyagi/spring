package com.exception;

public class BlockedClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BlockedClientException(String message) {
	super(message);
    }

}
