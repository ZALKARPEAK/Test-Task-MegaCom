package com.career.testtaskmegacom.api;

import com.career.testtaskmegacom.dto.Authentication.AuthenticationResponse;
import com.career.testtaskmegacom.dto.Authentication.SignInRequest;
import com.career.testtaskmegacom.dto.Authentication.SignUpRequest;
import com.career.testtaskmegacom.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationApi {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя по данным из тела запроса и возвращает JWT-токен."
    )
    public AuthenticationResponse register(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Проверяет данные пользователя и возвращает JWT-токен при успешной аутентификации."
    )
    public AuthenticationResponse authenticate(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }
}