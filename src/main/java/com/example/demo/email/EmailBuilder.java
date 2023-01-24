package com.example.demo.email;

public class EmailBuilder {
    public static String buildEmail(String recipientName, String verificationLink) {
        return "To confirm your email please click the following link: " + verificationLink;
    }
}
