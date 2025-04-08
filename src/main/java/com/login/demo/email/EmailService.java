package com.login.demo.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * This class is an implementation for an EmailSender service. This will be used to send verification emails to the user
 * so that they can enable their account.
 */
@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String fromAddress;

    /**
     * The error message to be thrown if an email fails to send.
     */
    private final static String FAILED_TO_SEND_MESSAGE = "Failed to send email";
    /**
     * The subject header for the emails.
     */
    private final static String SUBJECT_TEXT = "Safwan - Confirm your email";


    /**
     * Sends an email via SMTP using JavaMailSender.
     *
     * @param receiverAddress the recipient's email
     * @param message the body of the email
     */
    @Override
    @Async
    public void send(String receiverAddress, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(receiverAddress);
            mailMessage.setSubject(SUBJECT_TEXT);
            mailMessage.setText(message);
            mailMessage.setFrom(fromAddress);

            mailSender.send(mailMessage);
            LOGGER.info("Email sent successfully to {}", receiverAddress);
        } catch (Exception e) {
            LOGGER.error("Failed to send email to {}: {}", receiverAddress, e.getMessage(), e);
            throw new IllegalStateException(FAILED_TO_SEND_MESSAGE, e);
        }
    }


}
