package com.login.demo.email;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.EmailClientBuilder;
import com.azure.communication.email.models.EmailAddress;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * This class is an implementation for an EmailSender service. This will be used to send verification emails to the user
 * so that they can enable their account.
 */
@Service
@AllArgsConstructor
@ConditionalOnProperty(name = "config.is-email-enabled", havingValue = "true")
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Value("${azure.communication.email.connection-string}")
    private String connectionString;

    @Value("${azure.communication.email.sender-address}")
    private String senderAddress;

    /**
     * The error message to be thrown if an email fails to send.
     */
    private final static String FAILED_TO_SEND_MESSAGE = "Failed to send email";
    /**
     * The subject header for the emails.
     */
    private final static String SUBJECT_TEXT = "Safwan - Confirm your email";


    @PostConstruct
    public void init() {
        System.out.println("EmailService initialized (email is enabled)");
    }

    /**
     * This method will send an email with the specified message as the body to the recipient's email address
     * using Azure Communication Services
     *
     * @param receiverAddress the email address of the recipient (the user)
     * @param message the body of the email (can be formatted in plain text)
     */
    @Override
    @Async
    public void send(String receiverAddress, String message) {
        try {
            EmailClient emailClient = new EmailClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            EmailMessage emailMessage = new EmailMessage()
                    .setSenderAddress(senderAddress)
                    .setToRecipients(new EmailAddress(receiverAddress))
                    .setSubject(SUBJECT_TEXT)
                    .setBodyPlainText(message);

            SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage);
            PollResponse<EmailSendResult> response = poller.waitForCompletion();

            String messageId = response.getValue().getId();
            LOGGER.info("Email sent successfully. Message ID: {}", messageId);

        } catch (Exception e) {
            LOGGER.error("Failed to send email to {}: {}", receiverAddress, e.getMessage(), e);
            throw new IllegalStateException(FAILED_TO_SEND_MESSAGE, e);
        }
    }


}
