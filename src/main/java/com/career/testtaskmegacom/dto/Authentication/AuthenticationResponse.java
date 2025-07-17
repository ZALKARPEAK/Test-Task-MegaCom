package com.career.testtaskmegacom.dto.Authentication;

import com.career.testtaskmegacom.util.enums.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    String email;
    Role role;
}