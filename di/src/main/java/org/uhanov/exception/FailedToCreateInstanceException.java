package org.uhanov.exception;

public class FailedToCreateInstanceException extends RuntimeException{
    public FailedToCreateInstanceException() {
    }

    public FailedToCreateInstanceException(String message) {
        super(message);
    }
}
