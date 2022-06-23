package com.example.demo.email;

public class LinkBuilder {

    private final static String VERIFICATION_URL_PREFIX = "http://localhost:8080/api/v1/registration/confirm?token=";

    public static String getTokenVerificationLink(String tokenValue) {
        return VERIFICATION_URL_PREFIX + tokenValue;
    }
}
