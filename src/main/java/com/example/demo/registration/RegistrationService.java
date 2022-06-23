package com.example.demo.registration;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.appuser.AppUserService;
import com.example.demo.email.EmailBuilder;
import com.example.demo.email.EmailSender;
import com.example.demo.email.LinkBuilder;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final static Long EMAIL_RESEND_WAIT_TIME = 1L;

    public String register(RegistrationRequest request) {
        boolean emailIsValid = emailValidator.test(request.getEmail());

        if (!emailIsValid) {
            throw new IllegalStateException("Email is invalid");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        AppUser newUser = new AppUser(request.getFirstName(), request.getLastName(), request.getEmail(),
                encodedPassword, AppUserRole.USER);

        Optional <AppUser> existingUser = appUserService.getUserByEmail(newUser.getEmail());

        if (existingUser.isPresent()) {
            AppUser existingAppUser = existingUser.get();

            boolean passwordsAreSame =
                    bCryptPasswordEncoder.matches(request.getPassword(), existingAppUser.getPassword());
            boolean usersAreSame = existingAppUser.equals(newUser) && passwordsAreSame;

            if (existingAppUser.isEnabled() || !usersAreSame) {
                throw new IllegalStateException("email is already taken");
            }

            Optional<ConfirmationToken> existingToken = confirmationTokenService.getLatestToken(existingAppUser);
            if (existingToken.isPresent()) {
                ConfirmationToken latestToken = existingToken.get();
                if (latestToken.isValid() && latestToken.canBeResent()) {
                    // resend latest valid token
                    sendEmailToUser(latestToken, newUser);
                    return latestToken.getToken();
                }
                else {
                    confirmationTokenService.deleteToken(latestToken);
                }
            }
            ConfirmationToken confirmationToken = new ConfirmationToken(existingAppUser);
            confirmationTokenService.saveConfirmationToken(confirmationToken);
            sendEmailToUser(confirmationToken, existingAppUser);
            return confirmationToken.getToken();
        }

        appUserService.registerUser(newUser);

        ConfirmationToken confirmationToken = new ConfirmationToken(newUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        sendEmailToUser(confirmationToken, newUser);
        return confirmationToken.getToken();
    }

    private void sendEmailToUser(ConfirmationToken token, AppUser user) {
        String verificationLink = LinkBuilder.getTokenVerificationLink(token.getToken());
        emailSender.send(user.getEmail(), EmailBuilder.buildEmail(user.getFirstName(), verificationLink));
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "Email successfully confirmed";
    }
}
