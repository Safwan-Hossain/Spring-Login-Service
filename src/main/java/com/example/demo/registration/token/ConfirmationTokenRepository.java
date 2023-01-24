package com.example.demo.registration.token;

import com.example.demo.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * This repository contains functions for saving, querying and updating tokens.
 * @apiNote inherited methods save() and delete() can be used for saving and deleting.
 * @see ConfirmationToken
 */
@Repository
@Transactional(readOnly = true)
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    /**
     * Searches for a token by their token string value. The value is wrapped as an Optional.
     * @param token the string value of the ConfirmationToken
     * @return the ConfirmationToken wrapped inside an Optional if a token matching the string value was found.
     * Otherwise, returns an empty Optional.
     */
    Optional<ConfirmationToken> findByToken(String token);

    /**
     * Returns a list of all tokens that belong to a single user
     * @param appUser the user to search for
     * @return a list of ConfirmationTokens that belong to the user. If user owns no tokens, returns an empty list.
     */
    @Query(value = "SELECT c " +
            "FROM ConfirmationToken c " +
            "WHERE c.appUser = ?1 AND " +
            "c.confirmedAt IS NULL")
    List<ConfirmationToken> getAllTokensForUser(AppUser appUser);

    /**
     * Updates the confirmation time of a token. When a user confirms their account, this method will be used to update
     * their token.
     * @param token the string value of the token to update (service should check if token is expired)
     * @param confirmedAt the confirmation time of the token
     * @return 1 if the user is found and their values have been updated. 0 if no user is found or no values have been updated.
     * @implNote The repository does not allow multiple rows from having the same token value, so a value greater than one cannot be returned.
     */
    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);

}
