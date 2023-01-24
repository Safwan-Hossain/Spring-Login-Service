package com.example.demo.security.config;

import com.example.demo.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration for spring web security
 * @implNote Currently extending WebSecurityConfigurerAdapter which is deprecated. Will implement a more updated configuration.
 */
@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserService appUserService;
    private final String PERMITTED_URL_PATTERN = "/api/v*/registration/**";
    private final String SUCCESS_PAGE_URL = "/homepage.html";

    /**
     * Configuration for Spring Web Security. Permits any request from the given URL pattern.
     * CSRF protection is currently disabled so that POST requests are not rejected.
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs (specifically in {@link HttpSecurity#csrf() http.csrf()})
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers(PERMITTED_URL_PATTERN)
                .permitAll()
            .anyRequest()
            .authenticated().and()
            .formLogin()
                .defaultSuccessUrl(SUCCESS_PAGE_URL, true);
    }

    /**
     * Configuration for AuthenticationManagerBuilder. Uses DaoAuthenticationProvider as provider.
     * @param auth the {@link AuthenticationManagerBuilder} to use
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * Setup and return DaoAuthenticationProvider. Sets the password encoder and users details service for
     * the provider.
     * @return a DaoAuthenticationProvider Bean
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
}
