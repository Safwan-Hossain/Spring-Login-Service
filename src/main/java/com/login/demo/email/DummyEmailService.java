package com.login.demo.email;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "config.is-email-enabled", havingValue = "false")
public class DummyEmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(DummyEmailService.class);

    @PostConstruct
    public void init() {
        System.out.println("DummyEmailService initialized (email is disabled)");
    }
    @Override
    public void send(String to, String message) {
        LOGGER.info("[Email Disabled] Skipping sending to {}. Message: {}", to, message);
    }
}