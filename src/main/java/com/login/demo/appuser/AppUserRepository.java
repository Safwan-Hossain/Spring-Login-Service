package com.login.demo.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * This class is used to access the repository of existing users (AppUser entities).
 */
@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * This query is used to find the user's information (AppUser entity) in the database by searching for their email.
     * If the email is not found in the database then an empty Optional object will be returned.
     * @param email the email of the user
     * @return an Optional AppUser. If a matching email is found then the AppUser will be wrapped inside the Optional object.
     * Otherwise, an empty Optional object will be returned.
     */
    Optional<AppUser> findByEmail(String email);

    /**
     * When a user confirms their registration, this query will be used to enable their account.
     * The user is identified by the given email address. If the email address is not found in the database then no user will
     * be updated.
     * @param email the email of the user
     * @return the number of rows that were updated.
     */
    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.isEnabled = TRUE " +
            "WHERE a.email = ?1")
    int enableAppUser(String email);
}
