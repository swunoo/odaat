package com.odaat.odaat.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.odaat.odaat.config.BacklogOAuth2Config;
import com.odaat.odaat.dto.BacklogIssue;
import com.odaat.odaat.dto.ProjectIdAndName;
import com.odaat.odaat.model.AccessToken;
import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.service.AuthService;
import com.odaat.odaat.service.ProjectService;
import com.odaat.odaat.service.TaskService;
import com.odaat.odaat.utils.JsonUtil;

@Controller
public class SyncController {

    @Autowired
    BacklogOAuth2Config backlog;
    @Autowired
    AuthService authService;
    @Autowired
    ProjectService projectService;
    @Autowired
    TaskService taskService;

    // Reduce 100 seconds from expires_in
    // for the time elasped between receiving the response and storing the data
    int PAD_SECONDS = 100;

    @GetMapping("/sync/backlog") // Current: Poll this endpoint to get updates. // Future: Use webhooks.
    public ResponseEntity<?> oauth2Backlog() {

        try {
            // If we already have the token, try accessing with it.
            AccessToken tokenObject = authService.getTokenObject();
            if (tokenObject != null) {
                if (tokenObject.isValid())
                    return syncWithBacklog(tokenObject);
                else
                    return refreshTokenAndSyncWithBacklog(tokenObject);
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

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code, @RequestParam("state") String state) {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", backlog.getClientId());
        tokenRequest.put("client_secret", backlog.getClientSecret());
        tokenRequest.put("code", code);
        tokenRequest.put("redirect_uri", backlog.getRedirectUri());
        tokenRequest.put("grant_type", "authorization_code");

        String tokenUri = "https://nulab-exam.backlog.jp/api/v2/oauth2/token";
        Map<String, String> tokenResponse = restTemplate
                .postForObject(tokenUri, tokenRequest, Map.class);

        String accessToken = tokenResponse.get("access_token");
        String refreshToken = tokenResponse.get("refresh_token");
        int expiresIn = Integer.valueOf(tokenResponse.get("expires_in")); // TODO: NumberFormatException
        Instant expiryTime = Instant.now().plusSeconds(expiresIn - PAD_SECONDS);

        String backlogUserId = getUserId(accessToken);

        authService.addToken(backlogUserId, accessToken, refreshToken, expiryTime);

        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> refreshTokenAndSyncWithBacklog(AccessToken tokenObject) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("grant_type", "refresh_token");
        tokenRequest.put("client_id", backlog.getClientId());
        tokenRequest.put("client_secret", backlog.getClientSecret());
        tokenRequest.put("refresh_token", tokenObject.getRefreshToken());

        Map<String, String> tokenResponse = restTemplate.postForObject(
                backlog.getAppRoot() + backlog.getTokenUri(),
                tokenRequest,
                Map.class);

        String accessToken = tokenResponse.get("access_token");
        String refreshToken = tokenResponse.get("refresh_token");
        int expiresIn = Integer.valueOf(tokenResponse.get("expires_in")); // TODO: NumberFormatException
        Instant expiryTime = Instant.now().plusSeconds(expiresIn - PAD_SECONDS);

        tokenObject.refreshData(accessToken, refreshToken, expiryTime);

        return syncWithBacklog(tokenObject);
    }

    public ResponseEntity<?> syncWithBacklog(AccessToken tokenObject) throws Exception {

        String token = tokenObject.getToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<?> response;

        // 1. Sync Projects
        response = restTemplate.exchange(
                backlog.getProjects(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, String>>>() {
                });

        List<Map<String, String>> projects = (List<Map<String, String>>) response.getBody();

        // We need only "id" and "name" attributes to update our database.
        List<ProjectIdAndName> idAndNames = projects
                .stream()
                .map(p -> new ProjectIdAndName(Integer.valueOf(p.get("id")), p.get("name")))
                .collect(Collectors.toList());

        projectService.syncProjects(idAndNames);

        // 2. Sync issues
        response = restTemplate.exchange(
                backlog.getIssues(),
                HttpMethod.GET,
                entity,
                String.class);

        String issueString = (String) response.getBody();
        List<Map<String, Object>> issues = (List<Map<String, Object>>) JsonUtil.deserializeJson(issueString);

        List<BacklogIssue> backlogIssues = issues
                .stream()
                .map(issue -> {

                    Map<String, String> statusObject = (Map<String, String>) issue.get("status");
                    String statusName = statusObject.get("name");
                    Map<String, String> assigneeObject = (Map<String, String>) issue.get("assignee");
                    String assigneeId = String.valueOf(assigneeObject.get("id"));

                    if (statusName.equals("Closed") || !assigneeId.equals(tokenObject.getUserId())) {
                        return null;
                    }

                    Integer id = Integer.valueOf(issue.get("id").toString());
                    Integer projectId = Integer.valueOf(issue.get("projectId").toString());
                    String summary = issue.get("summary").toString();

                    Map<String, Object> priorityObject = (Map<String, Object>) issue.get("priority");
                    Integer priorityId = Integer.valueOf(priorityObject.get("id").toString());
                    Priority priority = priorityId == 2 ? Priority.HIGH
                            : priorityId == 4 ? Priority.LOW : Priority.MEDIUM;

                    Object estimated = issue.get("estimatedHours");
                    Object actual = issue.get("actualHours");
                    Double remainingHours;
                    try {
                        remainingHours = Double.valueOf(estimated.toString()) - Double.valueOf(actual.toString());
                    } catch (NullPointerException | NumberFormatException e) {
                        remainingHours = 0.0;
                    }

                    return new BacklogIssue(id, projectId, summary, priority, remainingHours);
                })
                .filter(issue -> issue != null)
                .collect(Collectors.toList());

        taskService.syncTasks(backlogIssues);

        return ResponseEntity.ok().build();
    }

    private String getUserId(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate.exchange(
                backlog.getOwnUser(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });

        Map<String, String> userData = (Map<String, String>) response.getBody();

        return userData.get("id");
    }

}
