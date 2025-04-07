package com.login.demo.log;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(ConsoleAppender.class, builder -> builder.withMembers());
        hints.reflection().registerType(PatternLayoutEncoder.class, builder -> builder.withMembers());
        hints.reflection().registerType(LoggerContext.class, builder -> builder.withMembers());
        hints.reflection().registerType(LoggingEvent.class, builder -> builder.withMembers());
    }
}