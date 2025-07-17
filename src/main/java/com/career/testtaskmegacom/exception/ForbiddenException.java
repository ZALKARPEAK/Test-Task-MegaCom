package com.career.testtaskmegacom.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public ForbiddenException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public ForbiddenException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }
}