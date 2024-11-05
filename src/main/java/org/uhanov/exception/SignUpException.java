package org.uhanov.exception;

public class SignUpException extends RuntimeException {
    public SignUpException() {
    }

    public SignUpException(String message) {
        super(message);
    }
}
