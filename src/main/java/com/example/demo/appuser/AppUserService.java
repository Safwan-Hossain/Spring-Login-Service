package com.example.demo.appuser;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "User with email %s not found.";
    private final static Long EMAIL_RESEND_WAIT_TIME = 1L;

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
    }

    public String registerUser(AppUser appUser) {
        Optional<AppUser> existingUser = appUserRepository.findByEmail(appUser.getEmail());

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        if (existingUser.isPresent()) {
//            AppUser existingAppUser = existingUser.get();
//            if (existingAppUser.equals(appUser)) {
//                if (appUser.isEnabled()) {
//                    throw new IllegalStateException("User already registered");
//                }
//                String userID = existingAppUser.getId().toString();
//
//                Optional<ConfirmationToken> existingToken = confirmationTokenService.getTokenByUserID(userID);
//                if (existingToken.isPresent()) {
//                    ConfirmationToken currentToken = existingToken.get();
//                    LocalDateTime tokenCreateTime = currentToken.getCreatedAt();
//                    LocalDateTime emailResendTime = LocalDateTime.now().minusMinutes(EMAIL_RESEND_WAIT_TIME);
//
//                    if (tokenCreateTime.isBefore(emailResendTime)) {
//
//                    }
//                }
//                throw new IllegalStateException("TOKEN ALREADY SENT");
//            }
            throw new IllegalStateException("email is already taken");
        }


        appUserRepository.save(appUser);

        ConfirmationToken confirmationToken = new ConfirmationToken(appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return confirmationToken.getToken();
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
