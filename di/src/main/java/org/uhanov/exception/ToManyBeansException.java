package org.uhanov.exception;

public class ToManyBeansException extends RuntimeException {
    public ToManyBeansException() {
    }

    public ToManyBeansException(String message) {
        super(message);
    }
}
