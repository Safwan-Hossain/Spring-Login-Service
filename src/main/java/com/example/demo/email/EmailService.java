package com.example.demo.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final static String FAILED_TO_SEND_MESSAGE = "Failed to send email";
    private final static String SUBJECT_TEXT = "Safwan - Confirm your email";
    private final static String SENDER_ADDRESS = "springloginservice@gmail.com";

    private final JavaMailSender mailSender;

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
            LOGGER.error(FAILED_TO_SEND_MESSAGE);
            throw new IllegalStateException(FAILED_TO_SEND_MESSAGE);
        }
    }


}
