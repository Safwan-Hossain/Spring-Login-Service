package com.login.demo.email;

/**
 * Provides functionality for sending emails.
 * Using an interface allows for multiple email sending implementations.
 * @see EmailService
 */
public interface EmailSender {
    /**
     * This method will send an email with the specified message as the body to the recipient's email address.
     * @param receiverAddress the email address of the recipient (the user)
     * @param message the text that is to be contained in the body of the email
     */
    void send(String receiverAddress, String message);
}
