package com.example.demo.appuser;

import com.example.demo.security.PasswordEncoder;
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
 * When a user first registers his/her information (name, email, encrypted password) is recorded into a database.
 * This information is used to identify who the current user is.
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
    @EqualsAndHashCode.Include
    private String email;
    private String password;

    /**
     * Identifies the current user's role (Admin or User).
     */
    @Enumerated(EnumType.STRING)
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
     * @implNote The user's raw password should not be used when creating an AppUser instance. The password should
     * rather be encrypted first.
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
     * Currently, account expiry has not been implemented. Therefore, this method will always return true
     * to indicate that the account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    /**
     * Indicates if a user is locked from access to his/her account. Currently, locking is not implemented.
     */
    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }


    /**
     * Indicates whether the user's password has expired.
     * Currently, password expiry has not been implemented. Therefore, this method will always return true
     * to indicate that the password is not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates if the user's account is enabled. A user's account is only enabled after they confirm their
     * account by clicking the verification link that will be emailed to them.
     * @return true if the user's account is enabled. Otherwise, false.
     * @implNote isEnabled is always initially set to false and is only set to true in the database level (when a user confirms their registration).
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean compareToUnsavedUser(AppUser unsavedUser, String password) {
        if (this == unsavedUser)
            return true;
        if (unsavedUser == null)
            return false;

        return unsavedUser.firstName.equalsIgnoreCase(this.firstName) &&
                unsavedUser.lastName.equalsIgnoreCase(this.lastName) &&
                unsavedUser.email.equalsIgnoreCase(this.email) &&
                password.equals(this.password);
    }
}
