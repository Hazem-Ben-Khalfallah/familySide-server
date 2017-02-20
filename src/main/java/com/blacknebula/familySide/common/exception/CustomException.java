package com.blacknebula.familySide.common.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = -2812473108740945538L;

    private final HttpStatus customErrorCode;

    public CustomException(HttpStatus errorCode) {
        super(errorCode.getReasonPhrase());
        this.customErrorCode = errorCode;
    }

    public CustomException(HttpStatus errorCode, String message, String... params) {
        super(String.format(message, params));
        this.customErrorCode = errorCode;
    }

    public CustomException(HttpStatus errorCode, Throwable e) {
        super(errorCode.getReasonPhrase(), e);
        this.customErrorCode = errorCode;
    }

    public CustomException(HttpStatus errorCode, Throwable e, String message, String... params) {
        super(String.format(message, params), e);
        this.customErrorCode = errorCode;
    }

    public HttpStatus getCustomErrorCode() {
        return customErrorCode;
    }
}