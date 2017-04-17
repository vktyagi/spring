package com.security.exceptions;

public class KeyStoreInitException extends Exception {

	/**
	 * Default id.
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs a KeyStoreException with no detail message.  (A
     * detail message is a String that describes this particular
     * exception.)
     */
	public KeyStoreInitException() {
        super();
    }

    /**
     * Constructs a KeyStoreException with the specified detail
     * message.  (A detail message is a String that describes this
     * particular exception.)
     *
     * @param msg the detail message.
     */
   public KeyStoreInitException(String msg) {
       super(msg);
    }

    /**
     * Creates a {@code KeyStoreException} with the specified
     * detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A {@code null} value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     * @since 1.5
     */
    public KeyStoreInitException(String message, Throwable cause) {
        super(message, cause);
    }

}
