package com.login.demo.registration.controllers;

import com.login.demo.appuser.AppUser;
import com.login.demo.registration.RegistrationRequest;
import com.login.demo.registration.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Registration controller for the login service. This controller will determine what should happen when
 * a request is made to the server. The user must first register their account, this will make their account considered
 * to be registered but the account is not yet enabled. After registration, the user will be emailed a confirmation link which
 * they must click before the link expires. Once they click the link, their account will be enabled, and they will be
 * redirected to a confirmation page.
 */
@Controller
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationMvcController {

    private final RegistrationService registrationService;

    @GetMapping
    public String register(Model model) throws IOException {
        model.addAttribute("registrationRequest", new RegistrationRequest());
//        response.sendRedirect("/templates/some/path/registration.html");
        return "registration";
    }

    /**
     * Registers a new user using the information from the request body.
     * @param request the request body
     * @return the confirmation token string value that belongs to the user.
     * @implNote If a registered user is to make a request to this endpoint again, then they will be resent their
     * confirmation token, or if that token has already expired then a new token will be generated and emailed back.
     * See {@link RegistrationService#register(RegistrationRequest) RegistrationService.register()} for more details.
     */
    @PostMapping
//    @ResponseBody
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
     * @return a success message when the user's account is successfully enabled.
     * @throws IllegalStateException when a user's account fails to enable. This may happen if the token does not exist,
     * if the token has already been confirmed before or if the token has expired.
     */
    @GetMapping(path = "confirm")
//    @ResponseBody
    public String confirm(@RequestParam("token") String token, Model model) {
        registrationService.confirmToken(token);
        return "confirmation-success";
    }


    @GetMapping("homepage")
    public String homepage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AppUser) {
            model.addAttribute("appUser", principal);
        }
        else {
            // If principal is null or is not an instance of AppUser then throw error
            throw new RuntimeException("User information not found."); // TODO change error
        }
        return "homepage";
    }

}
