package com.odaat.odaat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.odaat.odaat.config.BacklogOAuth2Config;
import com.odaat.odaat.model.AccessToken;
import com.odaat.odaat.service.AuthService;
import com.odaat.odaat.service.BacklogSyncService;

@RestController
public class SyncController {

    @Autowired
    BacklogOAuth2Config backlog;
    @Autowired
    AuthService authService;
    @Autowired
    BacklogSyncService backlogSync;

    /**
     * Baclolog OAuth2 synchronization. Currently, client should poll this endpoint.
     * In the future, webhooks will be used.
     * If a valid token is found, it uses it to synchronize with backlog.
     * Else, the user is redirected for OAuth2 authentication.
     *
     * @return a ResponseEntity object containing the result of the synchronization
     *         process or a RedirectView.
     */
    @GetMapping("/api/sync/backlog/on")
    public ResponseEntity<?> syncWithBacklog() {

        try {
            // Check if the token already exists
            AccessToken tokenObject = authService.getTokenObject();

            // If we already have the token, try accessing with it
            if (tokenObject != null) {

                // Sync data
                if(!tokenObject.isExpired()){
                    return backlogSync.syncWithBacklog(tokenObject);
                }

                 // If expired, refresh the token first
                if (tokenObject.getRefreshToken() != null){
                    backlogSync.refreshToken(tokenObject);
                    backlogSync.syncWithBacklog(tokenObject);
                } 

            }

            // If we don't have the token yet, the user is redirected for OAuth2
            String request = String.format(
                    "%s%s?response_type=code&client_id=%s&redirect_uri=%s&state=%s",
                    backlog.getAppRoot(),
                    backlog.getAuthorizationUri(),
                    backlog.getClientId(),
                    backlog.getRedirectUri(),
                    authService.getCurrentUserId());
            return new ResponseEntity<>(new RedirectView(request), HttpStatus.FOUND);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/api/sync/backlog/off")
    public ResponseEntity<?> unsyncWithBacklog() {
        authService.removeToken();
        return ResponseEntity.ok().body(Map.of("message", "success"));
    }

    /**
     * Handle the callback endpoint called from OAuth2 process.
     *
     * @param code  the authorization code received from backlog
     * @param state the state value received from backlog
     * @return a ResponseEntity object indicating the success of the callback
     *         process
     * @throws Exception if an error occurs during the callback process
     */
    @GetMapping("/api/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, @RequestParam("state") String state) {

        System.out.println("CALLBACK");
        System.out.println(code);

        try {
            // 1. Build the request to fetch token
            Map<String, String> tokenRequest = new HashMap<>();
            tokenRequest.put("client_id", backlog.getClientId());
            tokenRequest.put("client_secret", backlog.getClientSecret());
            tokenRequest.put("code", code);
            tokenRequest.put("redirect_uri", backlog.getRedirectUri());
            tokenRequest.put("grant_type", "authorization_code");

            // 2. Initialize access token for the user
            AccessToken currentToken = new AccessToken();
            authService.initToken(Integer.valueOf(state), currentToken);

            // 3. Send the request and update currentToken via the service
            backlogSync.getTokenAndSave(tokenRequest, currentToken);

            // KADAI: How to auto-close this blank page?
            return ResponseEntity.ok().body(Map.of("message", "Login successful. Please close this tab."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }


}