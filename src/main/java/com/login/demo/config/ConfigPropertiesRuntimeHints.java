package com.login.demo.config;

import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import java.util.List;

public class ConfigPropertiesRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
                .registerType(
                        TypeReference.of(ConfigProperties.class),
                        typeHint -> typeHint.withConstructor(
                                List.of(TypeReference.of(boolean.class)),
                                ExecutableMode.INVOKE
                        )
                );
    }
}