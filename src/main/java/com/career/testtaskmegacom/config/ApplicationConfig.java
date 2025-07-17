package com.career.testtaskmegacom.config;

import com.career.testtaskmegacom.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class ApplicationConfig {

    private final UserInfoRepository userInfoRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> (UserDetails) userInfoRepository.getUserAccountByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Email :" + email + " is not found")
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}