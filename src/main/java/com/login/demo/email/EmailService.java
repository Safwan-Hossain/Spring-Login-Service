package com.login.demo.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * This class is an implementation for an EmailSender service. This will be used to send verification emails to the user
 * so that they can enable their account.
 */
@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    /**
     * The error message to be thrown if an email fails to send.
     */
    private final static String FAILED_TO_SEND_MESSAGE = "Failed to send email";
    /**
     * The subject header for the emails.
     */
    private final static String SUBJECT_TEXT = "Safwan - Confirm your email";

//    private final static String SENDER_ADDRESS = "springloginservice@gmail.com";

    private final JavaMailSender mailSender;

    /**
     * This method will send an email with the specified message as the body to the recipient's email address.
     * The email's subject header is defined by a constant value so that every email will have the same subject.
     * @param receiverAddress the email address of the recipient (the user)
     * @param message the body of the email (can be formatted in HTML).
     */
    @Override
    @Async
    public void send(String receiverAddress, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(message);
            helper.setTo(receiverAddress);
            helper.setSubject(SUBJECT_TEXT);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException(FAILED_TO_SEND_MESSAGE);
        }
    }


}
