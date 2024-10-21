package org.uhanov.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {
        super();
    }

    public InvalidLoginException(String message) {
        super(message);
    }
}
