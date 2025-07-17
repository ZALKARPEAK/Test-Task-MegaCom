package com.career.testtaskmegacom.dto.Authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignInRequest {
    String email;
    String password;
}