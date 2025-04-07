package com.login.demo.registration;

import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import java.util.List;

public class RegistrationHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(
                TypeReference.of(RegistrationRequest.class),
                builder -> builder
                        .withMembers()
                        .withConstructor(List.of(), ExecutableMode.INVOKE)
                        .withConstructor(List.of(
                                TypeReference.of(String.class),
                                TypeReference.of(String.class),
                                TypeReference.of(String.class),
                                TypeReference.of(String.class)
                        ), ExecutableMode.INVOKE)
        );
    }
}