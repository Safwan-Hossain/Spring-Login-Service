package com.login.demo.registration;

import lombok.*;

/**
 * The request body for the registration request.
 * The fields are all the information required for a user to register.
 */
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class RegistrationRequest {
    /**
     * The inputted first name of the user
     */
    private String firstName;
    /**
     * The inputted last name of the user
     */
    private String lastName;
    /**
     * The inputted email address of the user
     */
    private String email;
    /**
     * The inputted (raw) password of the user
     * @implNote In a stricter application it may be more secure to encrypt the password before (client side) it is sent to the server
     * or use token generation instead. But in this project, the password is encrypted after its raw value is sent to the server.
     */
    private String password;

    /**
     * Constructor for RegistrationRequest.
     * @implNote the value of the names and email address are converted to lowercase before being set.
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param email the user's email
     * @param password the user's password
     */
    public RegistrationRequest(String firstName, String lastName, String email, String password) {
        // Using setters because we lowercase the string values. Removes repetition and allows future changes to be simpler.
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
    }

    /**
     * Sets the value of the user's first name.
     * @implNote the value of the name is converted to lowercase form before being set.
     * @param firstName the user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName.toLowerCase();
    }

    /**
     * Sets the value of the user's last name.
     * @implNote the value of the name is converted to lowercase form before being set.
     * @param lastName the user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName.toLowerCase();
    }

    /**
     * Sets the value of the user's email.
     * @implNote the value of the email address is converted to lowercase form before being set.
     * @param email the user's email address.
     */
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    /**
     * Sets the value of the user's password.
     * @param password the user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
