package com.smartstore.backend.security.config;

import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.*;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public BCryptPasswordEncoder
    passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

}