package com.career.testtaskmegacom.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse {
    private HttpStatus status;
    private String message;

    public CustomResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}