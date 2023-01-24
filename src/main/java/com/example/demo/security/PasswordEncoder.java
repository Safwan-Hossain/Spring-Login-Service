package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configures a bean for BCryptPasswordEncoder
 */
@Configuration
public class PasswordEncoder {


    /**
     * Used to get a BCryptPasswordEncoder bean that will be managed by Spring. E.g., an existing instance will
     * be returned or if no instance exists, then a new one will be constructed.
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
