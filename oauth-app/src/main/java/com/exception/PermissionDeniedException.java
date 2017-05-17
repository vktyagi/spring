package com.exception;

public class PermissionDeniedException extends Exception {
    private static final long serialVersionUID = 1L;

    public PermissionDeniedException() {
	super();
    }

    public PermissionDeniedException(String message) {
	super(message);
    }

}
