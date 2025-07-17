package com.career.testtaskmegacom.exception.handler;

import com.career.testtaskmegacom.exception.AlreadyExistsException;
import com.career.testtaskmegacom.exception.BadCredentialsException;
import com.career.testtaskmegacom.exception.JWTVerificationException;
import com.career.testtaskmegacom.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class AppExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse alreadyExistsException(AlreadyExistsException e){
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.CONFLICT)
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException e){
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse badCredentialException(BadCredentialsException e){
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.FORBIDDEN)
                .build();
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse jwtVerificationException(JWTVerificationException e) {
        return ExceptionResponse.builder()
                .message(messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()))
                .exceptionClassName(e.getClass().getSimpleName())
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .build();
    }
}