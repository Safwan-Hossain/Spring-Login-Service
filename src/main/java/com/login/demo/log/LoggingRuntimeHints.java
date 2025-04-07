package com.login.demo.log;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(TypeReference.of("ch.qos.logback.classic.Logger"), builder -> builder.withMembers());
        hints.reflection().registerType(TypeReference.of("ch.qos.logback.core.ConsoleAppender"), builder -> builder.withMembers());
    }
}