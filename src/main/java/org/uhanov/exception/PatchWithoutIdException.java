package org.uhanov.exception;

public class PatchWithoutIdException extends RuntimeException {
    public PatchWithoutIdException() {
    }

    public PatchWithoutIdException(String message) {
        super(message);
    }
}
