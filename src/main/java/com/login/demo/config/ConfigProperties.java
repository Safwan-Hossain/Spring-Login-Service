package com.login.demo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * Configuration properties that are read from the application.properties file. Reads any values with the defined prefix.
 */
@ConfigurationProperties(prefix = "config")
@Getter
public class ConfigProperties {
    /**
     * Should the application send a real email to the user?
     */
    private final boolean isEmailEnabled;

    @ConstructorBinding
    public ConfigProperties(boolean isEmailEnabled) {
        this.isEmailEnabled = isEmailEnabled;
    }
}
