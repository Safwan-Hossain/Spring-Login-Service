package com.login.demo.appuser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * This class is used to keep track of user data.
 * When a user first registers, his/her information (name, email, encrypted password) is stored inside this model class
 * and then recorded into a database.
 */
@Getter
@Setter
// Not all fields in this class can be used to identify a unique user.
// For example, it is not practical for the program to accept two users that have the same name and email but differing passwords.
// Therefore, this annotation uses the onlyExplicitlyIncluded flag to mark which fields should be used in the equals() method.
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "app_user_sequence"
    )
    private Long id;

    @EqualsAndHashCode.Include
    private String firstName;
    @EqualsAndHashCode.Include
    private String lastName;

    /**
     * The user's email is used to distinguish between other users and therefore must be unique
     */
    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    /**
     * Identifies the current user's role (Admin or User).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppUserRole appUserRole;

    /**
     * Indicates if a user is locked from access to his/her account.
     */
    private Boolean isLocked = false;

    /**
     * Indicates if the user has been successfully registered and confirmed their registration.
     * @implNote isEnabled is always initially set to false and is only set to true in the database level (when a user confirms their registration).
     */
    private Boolean isEnabled = false;

    public AppUser(String firstName,
                   String lastName,
                   String email,
                   String password,
                   AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    /**
     * Returns the authorities granted to the user (User or Admin). Cannot return null.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    /**
     * Get the user's password for the account
     * @implNote The user's password must be encrypted before creating an AppUser instance.
     * @return the user's password for their account
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Gets the email address of the user
     * @return the user's email address
     */
    @Override
    public String getUsername() { return email; }

    /**
     * Indicates whether the user's account has expired.
     * Currently, account expiry has not been implemented. Therefore, this method will always return true.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * Indicates if a user is locked from access to his/her account. Currently, locking is not implemented. Therefore,
     * this method will always return false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }


    /**
     * Indicates whether the user's password has expired.
     * Currently, password expiry has not been implemented. Therefore, this method will always return true.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates if the user's account is enabled. A user's account is only enabled after they confirm their
     * account by clicking the verification link that will be provided to them.
     * @return true if the user's account is enabled. Otherwise, false.
     * @implNote isEnabled is always initially set to false and is only set to true in the database level (after a user confirms their registration).
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
