package com.career.testtaskmegacom.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public NotFoundException(String messageCode, Object[] args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
    public NotFoundException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = null;
    }
}