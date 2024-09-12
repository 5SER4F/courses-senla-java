package org.uhanov.exception;

public class DbConnectionException extends RuntimeException {
    public DbConnectionException() {
    }

    public DbConnectionException(String message) {
        super(message);
    }
}
