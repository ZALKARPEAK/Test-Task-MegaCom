package com.career.testtaskmegacom.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleResponse {
    String messageCode;
    HttpStatus httpStatus;
}