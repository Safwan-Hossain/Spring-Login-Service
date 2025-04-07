package com.login.demo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.ImportRuntimeHints;

/**
 * Configuration properties that are read from the application.properties file. Reads any values with the defined prefix.
 */
@ImportRuntimeHints(ConfigPropertiesRuntimeHints.class)
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
