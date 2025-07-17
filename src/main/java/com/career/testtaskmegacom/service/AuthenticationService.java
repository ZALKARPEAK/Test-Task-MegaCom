package com.career.testtaskmegacom.service;

import com.career.testtaskmegacom.dto.Authentication.AuthenticationResponse;
import com.career.testtaskmegacom.dto.Authentication.SignInRequest;
import com.career.testtaskmegacom.dto.Authentication.SignUpRequest;

public interface AuthenticationService {
    AuthenticationResponse signUp(SignUpRequest request);
    AuthenticationResponse signIn(SignInRequest request);
}