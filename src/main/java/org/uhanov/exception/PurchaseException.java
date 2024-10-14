package org.uhanov.exception;

public class PurchaseException extends RuntimeException {
    public PurchaseException() {
    }

    public PurchaseException(String message) {
        super(message);
    }
}
