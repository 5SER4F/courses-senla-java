package org.uhanov.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException() {
        super();
    }

    public InvalidRoleException(String message) {
        super(message);
    }
}
