package com.career.testtaskmegacom.service.impl;

import com.career.testtaskmegacom.config.jwt.JwtService;
import com.career.testtaskmegacom.dto.Authentication.AuthenticationResponse;
import com.career.testtaskmegacom.dto.Authentication.SignInRequest;
import com.career.testtaskmegacom.dto.Authentication.SignUpRequest;
import com.career.testtaskmegacom.exception.AlreadyExistsException;
import com.career.testtaskmegacom.model.UserInfo;
import com.career.testtaskmegacom.repository.UserInfoRepository;
import com.career.testtaskmegacom.service.AuthenticationService;
import com.career.testtaskmegacom.util.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserInfoRepository userInfoRepository;

    @Override
    public AuthenticationResponse signUp(SignUpRequest request) {
        if (userInfoRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyExistsException("User already exists");
        }

        UserInfo userAccount = UserInfo.builder()
                .userInfoUserName(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userInfoRepository.save(userAccount);

        String jwt = jwtService.generateToken(userAccount);

        return AuthenticationResponse.builder()
                .email(userAccount.getEmail())
                .role(userAccount.getRole())
                .token(jwt)
                .build();
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest request) {
        UserInfo user = userInfoRepository.getUserAccountByEmail(request.getEmail()).orElseThrow(() ->
                new RuntimeException("email or password invalid"));

        String passwordBCrypt = request.getPassword();
        passwordEncoder.encode(passwordBCrypt);

        if (!passwordEncoder.matches(passwordBCrypt, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String jwt = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .token(jwt)
                .build();
    }
}