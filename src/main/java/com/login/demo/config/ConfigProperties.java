package com.login.demo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "config")
@ConstructorBinding
@Getter
@AllArgsConstructor
public class ConfigProperties {
    private final boolean isEmailEnabled;
    private final int tokenTimeoutMins;
    private final int tokenResendTimeoutMins;
}
