package org.uhanov.exception;

public class ToManyConstructorsException extends RuntimeException {

    public ToManyConstructorsException() {
    }
    public ToManyConstructorsException(String message) {
        super(message);
    }
}
