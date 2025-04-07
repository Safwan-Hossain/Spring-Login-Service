package com.login.demo.email;

/**
 * This class will be used to build the email body's structure and content.
 * After a user registers, a verification email will be sent to them. This class contains functionality for generating
 * the email content
 */
public class EmailBuilder {
    /**
     * This method creates an email body that asks the user to click a verification link so that they can
     * verify (enable) their account.
     * @implNote This method can be more complex and include HTML to properly structure the email body. But for
     * the purposes of this project, simple text is sufficient.
     * @param recipientName The name of the user
     * @param verificationLink the link that the user can click to verify their account
     * @return A string value that presents the user their verification link
     */
    public static String buildEmail(String recipientName, String verificationLink) {
        return """
            Hey %s,

            Click the link below to confirm your email:
            %s
            
            If you’d prefer not to click the link, that’s totally fine, it just means your account won’t be activated.
            Once activated, your email will be verified and you’ll be able to log in and access the dashboard.

            If you didn’t try to register, you can ignore this message.

            (This is an automated email - Please Do Not Reply)

            """.formatted(recipientName, verificationLink);
    }
}
