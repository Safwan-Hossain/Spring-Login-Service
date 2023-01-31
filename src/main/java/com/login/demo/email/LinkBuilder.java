package com.login.demo.email;


import com.login.demo.registration.token.ConfirmationToken;

/**
 * This class will create the URLs that are to be sent to the user so that they can verify their account.
 */
public class LinkBuilder {

    /**
     * Common URL prefix for links
     */
    // TODO - maybe move to app properties?
    private final static String VERIFICATION_URL_PREFIX = "http://localhost:8080/api/v1/registration/confirm?token=";

    /**
     * This method generates a URL for a user to click so that they will be redirected to a verification page. When a
     * new user registers, they will be assigned a uniquely generated confirmation token (see link below). Since the
     * token is unique and only belongs to a single user, their string value can be used in the URL to identify which
     * user is clicking the link. This way, the user does not need to log in again after clicking the confirmation link.
     * @param tokenValue The string value of the token for the specified user.
     * @return A string value for the verification link
     * @see ConfirmationToken
     */
    public static String getTokenVerificationLink(String tokenValue) {
        return VERIFICATION_URL_PREFIX + tokenValue;
    }
}
