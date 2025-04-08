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
        String capitalized = capitalize(recipientName);

        return """
            <html>
              <body style="font-family: sans-serif; color: #333; line-height: 1.6;">
                <p>Hey %s,</p>
                <p>Please confirm your email by clicking the link below:</p>
                <p><a href="%s">%s</a></p>
                <p>If you’d prefer not to click the link, that’s totally fine, it just means your account won’t be activated.</p>
                <p>Once activated, your email will be verified and you’ll be able to log in and access the dashboard.</p>
                <p style="font-size: small; color: #888;">This is an automated email – please do not reply.</p>
              </body>
            </html>
            """.formatted(capitalized, verificationLink, verificationLink);
    }


//  Capitalizes the first letter of a string.
//  If the input is null or empty, returns an empty string

    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) return "";
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
