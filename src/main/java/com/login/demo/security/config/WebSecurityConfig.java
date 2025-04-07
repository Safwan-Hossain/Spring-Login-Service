package com.login.demo.security.config;

import com.login.demo.appuser.AppUserService;
import com.login.demo.registration.RegistrationController;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.login.demo.constants.URLConstants.HOMEPAGE_PATH_SUBDIRECTORY;

/**
 * Configuration for spring web security
 * @implNote Currently extending WebSecurityConfigurerAdapter which is deprecated. Will implement a more updated configuration.
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserService appUserService;

    private final String LOGIN_PAGE_URL = "/login";

    private final String REGISTER_PAGE_URL = "/registration";
    private final String CONFIRMATION_PAGE_URL = "/registration/confirm";

    private final String PERMITTED_URL_PATTERN = "/api/v*/**";
    private final String STYLESHEET_URL_PATTERN = "/stylesheets/**";

    private final String SUCCESS_PAGE_URL = WebMvcLinkBuilder
            .linkTo(RegistrationController.class)
            .slash(HOMEPAGE_PATH_SUBDIRECTORY)
            .withSelfRel()
            .getHref();

    /**
     * Configuration for Spring Web Security. Permits any request from the given URL pattern.
     * CSRF protection is currently disabled so that POST requests are not rejected.
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs (specifically in {@link HttpSecurity#csrf() http.csrf()})
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                PERMITTED_URL_PATTERN,
                                STYLESHEET_URL_PATTERN,
                                LOGIN_PAGE_URL,
                                REGISTER_PAGE_URL,
                                CONFIRMATION_PAGE_URL
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(LOGIN_PAGE_URL)
                        .defaultSuccessUrl(SUCCESS_PAGE_URL, true)
                        .permitAll()
                )
                .build();
    }


    /**
     * Setup and return DaoAuthenticationProvider. Sets the password encoder and users details service for
     * the provider.
     * @return a DaoAuthenticationProvider Bean
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
}
