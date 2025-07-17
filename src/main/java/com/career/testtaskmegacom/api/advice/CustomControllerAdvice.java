package com.career.testtaskmegacom.api.advice;

import com.career.testtaskmegacom.api.response.CustomResponse;
import com.career.testtaskmegacom.exception.AlreadyExistsException;
import com.career.testtaskmegacom.exception.BadCredentialsException;
import com.career.testtaskmegacom.exception.ForbiddenException;
import com.career.testtaskmegacom.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @NonNull NoHandlerFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        String errorMessage = "no handler found for "
                + ex.getHttpMethod() + " " + ex.getRequestURL();
        return new ResponseEntity<>(new CustomResponse(
                HttpStatus.NOT_FOUND, errorMessage), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            @NonNull HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        StringBuilder errorMessage = new StringBuilder(ex.getMethod());
        errorMessage.append(" method is not supported for this request. supported methods are: ");
        Objects.requireNonNull(ex.getSupportedHttpMethods())
                .forEach(method -> errorMessage.append(method).append(" "));
        return new ResponseEntity<>(new CustomResponse(
                HttpStatus.METHOD_NOT_ALLOWED, errorMessage.toString()), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull TypeMismatchException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        String errorMessage = ex.getPropertyName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        return new ResponseEntity<>(new CustomResponse(HttpStatus.valueOf(status.value()), errorMessage), status);
    }

    @ExceptionHandler({
            NotFoundException.class,
            AlreadyExistsException.class,
            BadCredentialsException.class,
            ForbiddenException.class})
    public ResponseEntity<CustomResponse> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "something went wrong :(";

        if (ex instanceof BadCredentialsException) {
            status = HttpStatus.BAD_REQUEST;
            message = ex.getMessage();
        }

        if (ex instanceof AlreadyExistsException) {
            status = HttpStatus.CONFLICT;
            message = ex.getMessage();
        }

        if (ex instanceof ForbiddenException) {
            status = HttpStatus.FORBIDDEN;
            message = ex.getMessage();
        }

        if (ex instanceof NotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = ex.getMessage();
        }

        return new ResponseEntity<>(new CustomResponse(status, message), status);
    }
}