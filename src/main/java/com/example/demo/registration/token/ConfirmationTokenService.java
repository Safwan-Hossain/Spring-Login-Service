package com.example.demo.registration.token;

import com.example.demo.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Contains functionality for handling a ConfirmationToken. Allows the server to retrieve, save, delete, and update any
 * ConfirmationToken. This service acts mainly as an intermediary class for a controller and the ConfirmationTokenRepository.
 * @see ConfirmationTokenRepository
 */
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    /**
     * A repository that stores ConfirmationToken objects.
     */
    private final ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * Saves a ConfirmationToken to the repository.
     * @param token the ConfirmationToken that is to be saved
     */
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    /**
     * Deletes a ConfirmationToken from the repository.
     * @param token the ConfirmationToken that is to be deleted.
     */
    //TODO - ensure that a token is deleted by their token value
    public void deleteToken(ConfirmationToken token) {
        confirmationTokenRepository.delete(token);
    }

    /**
     * Searches for a token by their token string value. The value is wrapped as an Optional.
     * @param token the string value of the ConfirmationToken
     * @return the ConfirmationToken wrapped inside an Optional if a token matching the string value was found.
     * Otherwise, returns an empty Optional.
     */
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    /**
     * Gets the latest token that belongs to the user. In the event that a user's token has expired, they will need to
     * request a new token to be generated. In this scenario the user will own two tokens. This method will go through
     * all the tokens that belongs to a user and return the token with the latest expiry date
     * (the token that expires further into the future).
     * @param appUser the user
     * @return the latest ConfirmationToken (that belongs to the user) wrapped inside an Optional. If the user owns no
     * ConfirmationTokens then an empty Optional will be returned.
     */
    public Optional<ConfirmationToken> getLatestToken(AppUser appUser) {
        List<ConfirmationToken> tokens =  confirmationTokenRepository.getAllTokensForUser(appUser);

        if (tokens.isEmpty()) {
            return Optional.empty();
        }

        ConfirmationToken latestToken = tokens.get(0);
        for (ConfirmationToken token: tokens) {
            if (token.getExpiresAt().isAfter(latestToken.getExpiresAt())) {
                // The latest token is the token that expires later.
                latestToken = token;
            }
        }
        return Optional.of(latestToken);
    }


    /**
     * Updates the confirmation time of a token. When a user confirms their account, this method will be used to update
     * their token. The confirmation time will be set to the current time when this method is invoked.
     * @param token the string value of the token to update (service should check if token is expired)
     * @return The number of tokens confirmed (should return 1 or 0).
     * @implNote The repository does not allow multiple rows from having the same token value, so a value greater than 1 cannot be returned.
     */
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }


}
