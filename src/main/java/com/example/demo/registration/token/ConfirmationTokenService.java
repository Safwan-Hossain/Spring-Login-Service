package com.example.demo.registration.token;

import com.example.demo.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public void deleteToken(ConfirmationToken token) {
        confirmationTokenRepository.delete(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public Optional<ConfirmationToken> getLatestToken(AppUser appUser) {
        List<ConfirmationToken> tokens =  confirmationTokenRepository.getAllTokensForUser(appUser);

        if (tokens.isEmpty()) {
            return Optional.empty();
        }

        ConfirmationToken latestToken = tokens.get(0);
        for (ConfirmationToken token: tokens) {
            if (token.getExpiresAt().isAfter(latestToken.getExpiresAt())) {
                latestToken = token;
            }
        }

        return Optional.of(latestToken);
    }


    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }


}
