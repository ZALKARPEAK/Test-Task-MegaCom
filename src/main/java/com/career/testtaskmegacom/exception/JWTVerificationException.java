package com.career.testtaskmegacom.exception;

import lombok.Getter;

@Getter
public class JWTVerificationException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public JWTVerificationException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public JWTVerificationException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }
}