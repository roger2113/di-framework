package com.makarov.common.exception;

public class InvalidMethodSignature extends RuntimeException {

    public InvalidMethodSignature() {
    }

    public InvalidMethodSignature(String message) {
        super(message);
    }
}
