package com.login.demo.registration;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * This class is used to validate an email address.
 * For example, an email without a mail server (the value after the '@' symbol e.g. gmail) or
 * an email without a domain (the value after the '.' symbol e.g. com) is invalid.
 */
@Service
@AllArgsConstructor
public class EmailValidator implements Predicate<String>{
    /**
     * Regex that can check if an email is valid.
     * @implNote This regex was found on stack overflow (link provided below).
     * @see <a href="https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression">Regex</a>
     */
    private static final Pattern validEmailRegex = Pattern.compile
            ("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\" +
                    "x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a" +
                    "-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4]" +
                    "[0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[" +
                    "a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\" +
                    "x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");


    /**
     * This method checks if an email is valid or not by using a regex.
     * @param emailAddress the email address that is to be validated
     * @return true if the email address is valid. Otherwise, false.
     * @see #validEmailRegex
     */
    @Override
    public boolean test(String emailAddress) {
        // returns true if email is valid according to the regex
        return validEmailRegex.matcher(emailAddress).matches();
    }
}
