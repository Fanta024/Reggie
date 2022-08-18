package com.web.common;

public  class CustomException extends RuntimeException {
    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }
}
