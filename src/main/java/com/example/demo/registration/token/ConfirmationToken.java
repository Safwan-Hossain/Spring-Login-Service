package com.example.demo.registration.token;

import com.example.demo.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    // Default value for the number of minutes a token is valid for after being generated
    private static final int TOKEN_TIMEOUT_MINS = 15;

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

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;

    public ConfirmationToken(AppUser appUser, String token, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.appUser = appUser;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public ConfirmationToken(AppUser appUser) {
        this.appUser = appUser;
        this.token = generateTokenValue();
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(TOKEN_TIMEOUT_MINS);
    }

    public ConfirmationToken(AppUser appUser, String token) {
        this.appUser = appUser;
        this.token = token;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(TOKEN_TIMEOUT_MINS);
    }

    public static String generateTokenValue() {
        return UUID.randomUUID().toString();
    }

}
