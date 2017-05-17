package com.exception;

public class UserLockedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserLockedException(String message) {
	super(message);
    }

}
