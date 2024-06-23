package com.odaat.odaat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.odaat.odaat.config.BacklogOAuth2Config;
import com.odaat.odaat.model.BacklogAuth;
import com.odaat.odaat.service.AuthService;
import com.odaat.odaat.service.BacklogSyncService;

/*
    Controller to synchronize with Backlog.

    ## I. Description

    ### 1. OAuth2 Flow
    - In Backlog's OAuth2 flow, a user is first asked to give permissions via the Backlog's website.
    - If permissions are given, Backlog sends a callback to the application with [code].
    - The [code] is then used to request [authorization_code].
    - [authorization_code] is a Bearer token which is used to authorize other API requests.

    ### 2. My Implementation
    - External services and libraries might exist to handle everything automatically.
    - But, to understand the basics in-depth, I used custom services (AuthService, BacklogSyncService) and an entity (BacklogAuth) to facilitate OAuth2 flow.
    - AuthService stores the auth data for each user in memory.
    - BacklogAuth encapsulates relevant authentication data.
 
    ## II. Endpoints
    
    ### 1. "/api/sync/backlog/on" - Start syncing
     - If the user has a valid token, that token is used to sync data.
    - If the user has an expired token, the token is refreshed, and then used to sync data.
    - If the user doesn't have a token, they are redirected to Backlog's OAuth2 login.
    
    ### 2. "/api/sync/backlog/off" - Stop syncing
    - The user's token is removed, stopping the data from being synced.
    
    ### 3. "/api/callback" - Use Backlog's [code] to request [authorization_code]
    - Endpoint is called by Backlog with [code] and [state] parameters.
    - "code" parameter is used to request an [authorization_code]
    - Upon receiving the [authorization_code], it is converted to an AccessToken object and stored for the relevant user as specified by [state].
    - If successful, user is redirected back to the root Url.
    - If uncessful, the error is returned.

    ## III. Reference

    - https://developer.nulab.com/docs/backlog/auth/
    
    
 */
@RestController
public class SyncController {

    @Autowired
    BacklogOAuth2Config backlog;
    @Autowired
    AuthService authService;
    @Autowired
    BacklogSyncService backlogSync;

    @GetMapping("/api/sync/backlog/on")
    public ResponseEntity<?> syncWithBacklog() {

        try {
            // Check if the token already exists
            BacklogAuth tokenObject = authService.getTokenObject();

            // If we already have the token, try accessing with it
            if (tokenObject != null) {

                // Sync data
                if(!tokenObject.isExpired()){
                    return backlogSync.syncWithBacklog(tokenObject);
                }

                 // If expired, refresh the token first
                if (tokenObject.getRefreshToken() != null){
                    backlogSync.refreshToken(tokenObject);
                    return backlogSync.syncWithBacklog(tokenObject);
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
        Callback endpoint called from Backlog's OAuth2 process.
     *
        @param code  the authorization code received from backlog
        @param state the state value received from backlog
        @return a ResponseEntity object indicating the success of the callback
                process
        @throws Exception if an error occurs during the callback process
     */
    @GetMapping("/api/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, @RequestParam("state") String state) {

        try {
            // 1. Build the request to fetch token
            Map<String, String> tokenRequest = new HashMap<>();
            tokenRequest.put("client_id", backlog.getClientId());
            tokenRequest.put("client_secret", backlog.getClientSecret());
            tokenRequest.put("code", code);
            tokenRequest.put("redirect_uri", backlog.getRedirectUri());
            tokenRequest.put("grant_type", "authorization_code");

            // 2. Initialize access token for the user
            BacklogAuth currentToken = new BacklogAuth();
            authService.initToken(state, currentToken);

            // 3. Send the request and update currentToken via the service
            backlogSync.getTokenAndSave(tokenRequest, currentToken);

            return new ResponseEntity<>(new RedirectView("/"), HttpStatus.OK);
            
            // return ResponseEntity.ok().body(Map.of("message", "Login successful. Please close this tab."));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }


}