package com.login.demo.registration.token;

import com.login.demo.appuser.AppUser;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

import java.util.List;

public class ConfirmationTokenRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
                .registerType(TypeReference.of(ConfirmationToken.class), type -> type
                        .withConstructor(List.of(), ExecutableMode.INVOKE)
                        .withConstructor(List.of(TypeReference.of(AppUser.class)), ExecutableMode.INVOKE)
                        .withMembers()
                );
    }
}