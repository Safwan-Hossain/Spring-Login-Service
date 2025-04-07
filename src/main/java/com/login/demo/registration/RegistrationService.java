package com.login.demo.registration;

import com.login.demo.appuser.AppUser;
import com.login.demo.appuser.AppUserRole;
import com.login.demo.appuser.AppUserService;
import com.login.demo.email.EmailBuilder;
import com.login.demo.email.EmailSender;
import com.login.demo.registration.token.ConfirmationToken;
import com.login.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.login.demo.constants.URLConstants.CONFIRMATION_PATH_SUBDIRECTORY;
import static com.login.demo.constants.URLConstants.TOKEN_REQ_PARAMETER;

/**
 * A service used to register and enable a new user's account. The user must first register their account,
 * this will make their account considered to be registered but the account is not yet enabled.
 * After registration, the user will be emailed a confirmation link which they must click before the link expires.
 * Once they click the link, their account will be enabled, and they will be redirected to a confirmation page.
 */
@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    /**
     * Password encryptor
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Register a new user. If the requesting user's (the user sending the request) email address
     * does not exist in the database then save the user to the database, generate a token for them, and email them the token.
     * If the requesting user's email address already exist then throw an error.
     * @param request the request body
     * @return a verification link for the user
     * @throws IllegalStateException if the user's email address is invalid or the email address is already taken by another user.
     */
    public String register(RegistrationRequest request) {
        boolean emailIsValid = emailValidator.test(request.getEmail());

        if (!emailIsValid) {
            throw new IllegalStateException("Email is invalid");
        }

        // Encrypt password and create new AppUser using the encrypted password (avoids storing raw password values in the database)
        String encryptedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        AppUser newUser = new AppUser(request.getFirstName(), request.getLastName(), request.getEmail(), encryptedPassword, AppUserRole.USER);

        // Search if this email is already registered (already exists in repository)
        Optional<AppUser> existingUser = appUserService.getUserByEmail(newUser.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email is already taken");
        }

        appUserService.registerUser(newUser);
        // generate new token
        ConfirmationToken confirmationToken = new ConfirmationToken(newUser);
        // save the token to repository
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        // email the confirmation link (with the token) to the user
        String confirmationLink = getTokenConfirmationLink(confirmationToken.getToken());
        sendEmailToUser(newUser, confirmationLink);
        return confirmationLink;
    }

    /**
     * Confirm a token given a string token value and then enable the user's account. This method will first find the
     * ConfirmationToken given its token value. If the token exists and is valid it will confirm the token and then
     * enable the user's account (AppUser) that owns this token.
     * @param token the String value of the ConfirmationToken that is to be confirmed.
     * @throws IllegalStateException when a user's account fails to enable. This can happen if the token does not exist,
     * if the token has already been confirmed before, or if the token has expired.
     */
    @Transactional
    public void confirmToken(String token) {
        // If the ConfirmationToken is not found throw an error
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        // If the ConfirmationToken has already been confirmed throw an error
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        boolean tokenIsExpired = expiredAt.isBefore(LocalDateTime.now());
        if (tokenIsExpired) {
            throw new IllegalStateException("token expired");
        }

        // Set token confirmation time and enable the user's account
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUserOwner().getEmail());
    }

    /**
     * This method is used to resend a user their token (via email). If the user has an existing token that can be
     * resent, then reuse that token. Otherwise, generate a new token and send the new token to the user.
     * @param existingAppUser the AppUser requesting the token
     * @return If the user has an existing token that can be resent, then return that token.
     * Otherwise, generate and return a new token.
     */
    private String sendExistingOrNewTokenToUser(AppUser existingAppUser) {
        Optional<ConfirmationToken> existingToken = confirmationTokenService.getLatestToken(existingAppUser);
        if (existingToken.isPresent()) {
            ConfirmationToken latestToken = existingToken.get();
            if (latestToken.isNonExpired() && latestToken.canBeResent()) {
                // resend latest valid token
                String confirmationLink = getTokenConfirmationLink(latestToken.getToken());
                sendEmailToUser(existingAppUser, confirmationLink);
                return confirmationLink;
            }
            else {
                // TODO - delete all tokens that belong to the user
                confirmationTokenService.deleteToken(latestToken);
            }
        }
        ConfirmationToken confirmationToken = new ConfirmationToken(existingAppUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String confirmationLink = getTokenConfirmationLink(confirmationToken.getToken());
        sendEmailToUser(existingAppUser, confirmationLink);
        return confirmationLink;
    }

    /**
     * Emails a user their verification link if emailing is enabled for this application. If emailing is disabled, this
     * method will do nothing.
     * @param user the user requesting to be registered
     * @param verificationLink the user's verification link
     */
    private void sendEmailToUser(AppUser user, String verificationLink) {
        emailSender.send(user.getEmail(), EmailBuilder.buildEmail(user.getFirstName(), verificationLink));
    }

    /**
     * This method generates a URL for a user to click so that they will be redirected to a verification page. When a
     * new user registers, they will be assigned a uniquely generated confirmation token (see link below). Since the
     * token is unique and only belongs to a single user, their string value can be used in the URL to identify which
     * user is clicking the link. This way, the user does not need to log in again after clicking the confirmation link.
     * @param tokenValue The string value of the token for the specified user.
     * @return A string value for the verification link
     * @see ConfirmationToken
     */
    private String getTokenConfirmationLink(String tokenValue) {
        // WebMvcLinkBuilder does not support methods with query parameters.
        // First generate the basic link without the parameter
        String confirmationLink = WebMvcLinkBuilder.linkTo(RegistrationController.class).slash(CONFIRMATION_PATH_SUBDIRECTORY).withSelfRel().getHref();
        // Then append query parameter (token value) to the link
        confirmationLink += "?" + TOKEN_REQ_PARAMETER + "=" + tokenValue;
        return confirmationLink;
    }
}
