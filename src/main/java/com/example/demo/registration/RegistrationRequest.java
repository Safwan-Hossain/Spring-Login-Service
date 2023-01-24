package com.example.demo.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * The request body for the registration request.
 * The fields are all the information required for a user to register.
 */
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
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
}
