package com.login.demo.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class contains functionality for handling an AppUser.
 * Methods such as finding an existing user, registering a new user and enabling a user are defined here.
 */
@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE = "User with email %s not found.";

    private final AppUserRepository appUserRepository;

    /**
     * Tries to find a user in the database that matches the specified username. If the user is not found, throw an error.
     * @param email the username identifying the user whose data is required.
     * @return the AppUser entity that belongs to the user
     * @throws UsernameNotFoundException if the email is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, email)));
    }

    /**
     * Tries to find a user in the database that matches the specified email. If the user is not found, return an empty Optional object.
     * @param email the email identifying the user whose data is required.
     * @return an AppUser entity whose email matches the specified email wrapped in an Optional object.
     * If no emails match then return an empty Optional object.
     */
    public Optional<AppUser> getUserByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }

    /**
     * This method is used to check if the email is already registered to another user (including unverified users).
     * @param email the email identifying the user whose data is required.
     * @return true if this email already exists. Otherwise, false.
     * @implNote if a user registers but does NOT confirm their registration, their email will still be reserved so that
     * another user cannot register with the same email.
     */
    public boolean emailIsTaken(String email) {
        return appUserRepository.findByEmail(email).isPresent();
    }

    /**
     * This method is used to register a new user. Their data is saved into the repository but their account is not enabled
     * until they confirm their email.
     * @param appUser the AppUser that is to be registered/saved into the repository.
     * @throws java.sql.SQLException if the user's email already exists in the repository.
     */
    public void registerUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    /**
     * This method is used to enable the account of a registered user. After a new user registers their account and
     * confirms their email, their account will be enabled. This method finds the registered user in the database
     * by their email address and then enables their account.
     * @param email the email identifying the user whose data is required.
     * @return 1 if the user is found and their values have been updated. 0 if no user is found or no values have been updated.
     * @implNote The repository does not allow multiple rows from having the same email, so a value greater than one cannot be returned.
     */
    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
