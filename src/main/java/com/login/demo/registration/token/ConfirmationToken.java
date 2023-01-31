package com.login.demo.registration.token;

import com.login.demo.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * This entity class is used to generate tokens that can be used to enable/verify a user's account.
 * The token will keep track of its owner (the AppUser it belongs to) as well as the date-time it was created, expired
 * and/or confirmed (by the user).
 * The token will be saved into a repository so that when the user confirms their email, the saved data can be used to
 * determine whether the token is valid or not (if the token has already expired or confirmed).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    /**
     * Default value for the number of minutes a token is valid for after being generated
     */
    @Transient
    private static final int TOKEN_TIMEOUT_MINS = 3;

    /**
     * Default value for the number of minutes after being generated a token can be resent to the same user
     */
    @Transient
    private static final int TOKEN_RESEND_TIMEOUT_MINS = 1;


    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;

    /**
     * The string value of the token
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * The date and time of when the token was generated.
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * The future date and time of when the token WILL expire.
     * If the user does not confirm before this time then their token will be expired.
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * The date and time of when a token was confirmed (if confirmed).
     * If a user successfully confirms their account before their link expires, then this field will record
     * when their account was confirmed.
     */
    private LocalDateTime confirmedAt;

    /**
     * Indicates the AppUser owner for this token.
     * This field is assigned a ManyToOne relationship because any AppUser can own multiple tokens, but every token
     * must have exactly one AppUser.
     * This is done so that if a token expires, then the user can request another token to be generated.
     * In this case both the tokens belong to the user.
     */
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUserOwner;

    /**
     * Creates an instance that automatically generates a token value, records the creation time and records the
     * expected expiry time. Assigns the owner as the user provided.
     * @param appUserOwner the user that owns this token
     */
    public ConfirmationToken(AppUser appUserOwner) {
        this.appUserOwner = appUserOwner;
        this.token = generateTokenValue();
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(TOKEN_TIMEOUT_MINS);
    }

    /**
     * Generates a random unique string value.
     * @return the string value for this token
     */
    public static String generateTokenValue() {
        return UUID.randomUUID().toString();
    }

    /**
     * Checks if this token has expired.
     * @return true if the token has not been expired, false if otherwise.
     */
    public boolean isNonExpired() {
        return LocalDateTime.now().isBefore(this.expiresAt);
    }

    /**
     * Checks if the current token can be reused. If a user requests the confirmation link to be sent again, then the same
     * token can be reused if the token has not expired and the token's resend window has not been closed. If a new token
     * is to be generated everytime the same user requests to be resent the verification link, then it will be possible to
     * generate multiple tokens for a single user in less than one minute. This is not ideal. This method will help limit
     * the amount of newly generated tokens, saving space in the token repositories.
     * @return true if the token is not expired and can be resent. Otherwise, false.
     */
    public boolean canBeResent() {
        if (!isNonExpired()) {
            return false;
        }
        // The window of time a token can be resent (e.g. 5 mins after being created)
        LocalDateTime tokenResendExpiryTime = this.getCreatedAt().plusMinutes(TOKEN_RESEND_TIMEOUT_MINS);
        return LocalDateTime.now().isBefore(tokenResendExpiryTime);
    }
}
