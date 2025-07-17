package com.career.testtaskmegacom.dto.Authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {
    String username;
    String email;
    String password;
}