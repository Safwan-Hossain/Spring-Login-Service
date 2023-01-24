package com.example.demo.email;

/**
 * This class will be used to build the email body's structure and content.
 * After a user registers, a verification email will be sent to them. This class contains functionality for generating
 * the email content
 */
public class EmailBuilder {
    /**
     * This method creates an email body that asks the user to click a verification link so that they can
     * verify (enable) their account.
     * NOTE - This method can be much more complex and can include HTML to properly structure the email body. But for
     * the purposes of this project, simple text is sufficient.
     * @param recipientName The name of the user
     * @param verificationLink the link that the user can click to verify their account
     * @return A string value that presents the user their verification link
     */
    public static String buildEmail(String recipientName, String verificationLink) {
        return "To confirm your email please click the following link: " + verificationLink;
    }
}
