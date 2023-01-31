package com.login.demo.registration;

import com.login.demo.appuser.AppUser;
import com.login.demo.constants.URLConstants;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.login.demo.constants.URLConstants.CONTROLLER_PATH_PREFIX;

/**
 * MVC controller for the login/registration service. This controller will determine what should happen when
 * a request is made to the server. The user must first register their account, this will make their account considered
 * to be registered but the account is not yet enabled. After registration, the user will be emailed a confirmation link which
 * they must click before the link expires. Once they click the link, their account will be enabled, and they will be
 * redirected to a confirmation page.
 */
@Controller
@RequestMapping(path = CONTROLLER_PATH_PREFIX)
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * GET mapping for registering. This method will be invoked when the user visits the registration URL. They will then
     * be redirected to the registration view.
     * @param model the Spring context model. Used to create a RegistrationRequest Object.
     * @return the view (HTML page) for registering.
     */
    @GetMapping(path = URLConstants.REGISTRATION_PATH_SUBDIRECTORY)
    public String register(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "registration";
    }

    /**
     * Registers a new user using the information from the request body (RegistrationRequest).
     * @param request the request body
     * @param model the Spring context model. Used to get the RegistrationRequest Object.
     * @return a view presenting the verification link for the user.
     * See {@link RegistrationService#register(RegistrationRequest) RegistrationService.register()} for more details.
     */
    @PostMapping(path = URLConstants.REGISTRATION_PATH_SUBDIRECTORY)
    public String register(@ModelAttribute RegistrationRequest request, Model model) {
        String verificationLink = registrationService.register(request);
        model.addAttribute("verificationLink", verificationLink);
        return "verification";
    }


    /**
     * Enables a registered user's account using their confirmation token. After a new user registers, they will be sent
     * an email with a link to confirm (enable) their account. The user's token value will be used as a path parameter
     * in the link's URL. This way we can identify which user clicked the link without forcing them to log in again.
     * @param token the string value of the user's ConfirmationToken (this will be received as a path parameter).
     * @return a view indicating success.
     * @throws IllegalStateException when a user's account fails to enable. This may happen if the token does not exist,
     * if the token has already been confirmed before or if the token has expired.
     */
    @GetMapping(path = URLConstants.CONFIRMATION_PATH_SUBDIRECTORY)
    public String confirm(@RequestParam(URLConstants.TOKEN_REQ_PARAMETER) String token) {
        registrationService.confirmToken(token);
        return "confirmation-success";
    }


    /**
     * GET mapping for the homepage of the application. Once a user successfully logs in, they will be redirected to the
     * homepage where a success message will be displayed. If no user is logged in and tries to access this endpoint
     * the application will throw an error.
     * @param model the Spring context model. Used to display user information in the view.
     * @return a view for the homepage
     * @throws AccessDeniedException if there is no authenticated user logged in the current session.
     * Note that this exception will redirect the user back to the login page.
     */
    @GetMapping(path = URLConstants.HOMEPAGE_PATH_SUBDIRECTORY)
    public String homepage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUser) {
            model.addAttribute("appUser", principal);
        }
        else {
            // If principal is null or is not an instance of AppUser then throw error
            throw new AccessDeniedException("User information not found. Please log in.");
        }
        return "homepage";
    }

}
