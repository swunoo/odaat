package com.odaat.odaat.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;

import com.odaat.odaat.config.BacklogOAuth2Config;
import com.odaat.odaat.model.BacklogAuth;
import com.odaat.odaat.service.AuthService;
import com.odaat.odaat.service.BacklogSyncService;

@ExtendWith(MockitoExtension.class)
public class SyncControllerTest {

    @Mock
    private BacklogOAuth2Config backlog;

    @Mock
    private AuthService authService;

    @Mock
    private BacklogSyncService backlogSyncService;

    @InjectMocks
    private SyncController syncController;

    private BacklogAuth validToken;
    private BacklogAuth expiredToken;

    @BeforeEach
    public void setUp() {
        validToken = new BacklogAuth();
        validToken.setToken("validToken");
        validToken.setUserId("userId");
        validToken.setExpiresAt(Instant.now().plusSeconds(3600)); // valid for 1 hour

        expiredToken = new BacklogAuth();
        expiredToken.setToken("expiredToken");
        expiredToken.setUserId("userId");
        expiredToken.setRefreshToken("refreshToken");
        expiredToken.setExpiresAt(Instant.now().minusSeconds(3600)); // expired 1 hour ago
    }

    @Test
    public void testSyncWithBacklog_WithValidToken() throws Exception {
        when(authService.getTokenObject()).thenReturn(validToken);
        when(backlogSyncService.syncWithBacklog(validToken)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = syncController.syncWithBacklog();

        verify(backlogSyncService, times(1)).syncWithBacklog(validToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSyncWithBacklog_WithExpiredToken() throws Exception {
        when(authService.getTokenObject()).thenReturn(expiredToken);
        doNothing().when(backlogSyncService).refreshToken(expiredToken);
        when(backlogSyncService.syncWithBacklog(expiredToken)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = syncController.syncWithBacklog();

        verify(backlogSyncService, times(1)).refreshToken(expiredToken);
        verify(backlogSyncService, times(1)).syncWithBacklog(expiredToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSyncWithBacklog_NoToken() throws Exception {
        when(authService.getTokenObject()).thenReturn(null);
        when(backlog.getAppRoot()).thenReturn("https://app.root/");
        when(backlog.getAuthorizationUri()).thenReturn("authorize");
        when(backlog.getClientId()).thenReturn("clientId");
        when(backlog.getRedirectUri()).thenReturn("https://redirect.uri/");
        when(authService.getCurrentUserId()).thenReturn("123");

        ResponseEntity<?> response = syncController.syncWithBacklog();

        verify(backlogSyncService, times(0)).syncWithBacklog(any());
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof RedirectView);
        RedirectView redirectView = (RedirectView) response.getBody();
        assertEquals("https://app.root/authorize?response_type=code&client_id=clientId&redirect_uri=https://redirect.uri/&state=123", redirectView.getUrl());
    }

    @Test
    public void testSyncWithBacklog_Exception() throws Exception {
        when(authService.getTokenObject()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = syncController.syncWithBacklog();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    public void testUnsyncWithBacklog() {
        doNothing().when(authService).removeToken();

        ResponseEntity<?> response = syncController.unsyncWithBacklog();

        verify(authService, times(1)).removeToken();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCallback() throws Exception {
        String code = "authCode";
        String state = "123";
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", "clientId");
        tokenRequest.put("client_secret", "clientSecret");
        tokenRequest.put("code", code);
        tokenRequest.put("redirect_uri", "https://redirect.uri/");
        tokenRequest.put("grant_type", "authorization_code");

        when(backlog.getClientId()).thenReturn("clientId");
        when(backlog.getClientSecret()).thenReturn("clientSecret");
        when(backlog.getRedirectUri()).thenReturn("https://redirect.uri/");
        doNothing().when(authService).initToken(eq(state), any(BacklogAuth.class));
        doNothing().when(backlogSyncService).getTokenAndSave(eq(tokenRequest), any(BacklogAuth.class));

        ResponseEntity<?> response = syncController.callback(code, state);

        verify(authService, times(1)).initToken(eq(state), any(BacklogAuth.class));
        verify(backlogSyncService, times(1)).getTokenAndSave(eq(tokenRequest), any(BacklogAuth.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCallback_Exception() throws Exception {
        String code = "authCode";
        String state = "123";

        when(backlog.getClientId()).thenReturn("clientId");
        when(backlog.getClientSecret()).thenReturn("clientSecret");
        when(backlog.getRedirectUri()).thenReturn("https://redirect.uri/");
        doNothing().when(authService).initToken(eq(state), any(BacklogAuth.class));
        doThrow(new RuntimeException("Error")).when(backlogSyncService).getTokenAndSave(any(), any());

        ResponseEntity<?> response = syncController.callback(code, state);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
}