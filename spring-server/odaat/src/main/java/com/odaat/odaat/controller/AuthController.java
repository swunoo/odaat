package com.odaat.odaat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.odaat.odaat.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/*
  Handle authentication-related requests from the client.
  "/api/user"  -   Check if a user is authenticated.
  "/api/logout"-   Clear the session and return the logout link.
  
  References:
  - https://auth0.com/docs/authenticate/login/logout/log-users-out-of-auth0
  - https://auth0.com/blog/simple-crud-react-and-spring-boot/
  
 */
@RestController
public class AuthController {
    private final ClientRegistration registration;

    @Autowired
    private AuthService authService;

    public AuthController(ClientRegistrationRepository registrations) {
        this.registration = registrations.findByRegistrationId("okta");
    }

    @GetMapping("/api/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {

            authService.getCurrentUser();

            return ResponseEntity.ok().body(user.getAttributes());
        }
    }

    @PostMapping("/api/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
            @AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken) {

        // Get issuerUri of oauth2 provider
        String issuerUri = this.registration.getProviderDetails().getIssuerUri();
                
        // Build logoutUrl string
        StringBuilder logoutUrl = new StringBuilder();
        logoutUrl.append(issuerUri);
        if(!issuerUri.endsWith("/")) logoutUrl.append("/");
        logoutUrl.append("v2/logout");
        logoutUrl.append("?client_id=").append(this.registration.getClientId());

        Map<String, String> logoutDetails = Map.of("logoutUrl", logoutUrl.toString());
        
        // Invalidate current session
        request.getSession(false).invalidate();
        
        // Return a response with the url
        return ResponseEntity.ok().body(logoutDetails);
    }
}