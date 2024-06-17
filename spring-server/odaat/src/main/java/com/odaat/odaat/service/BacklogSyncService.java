package com.odaat.odaat.service;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.odaat.odaat.config.BacklogOAuth2Config;
import com.odaat.odaat.dto.BacklogIssue;
import com.odaat.odaat.dto.ProjectIdAndName;
import com.odaat.odaat.model.AccessToken;
import com.odaat.odaat.model.Task;
import com.odaat.odaat.model.enums.Priority;
import com.odaat.odaat.utils.JsonUtil;

/* Communicate with backlog and pass data to local services */
@Service
public class BacklogSyncService {
    @Autowired AuthService authService;
    @Autowired CategoryService categoryService;
    @Autowired ProjectService projectService;
    @Autowired TaskService taskService;
    @Autowired BacklogOAuth2Config backlog;
    @Autowired LocalSyncService localSync;

    // Reduce 100 seconds from expires_in,
    // for the time elasped between receiving the response and storing the data
    int PAD_SECONDS = 100;

    private RestTemplate restTemplate = new RestTemplate();

    /*
     * Make a token request based on the request body, and save the response in currentToken.
     * Used in both fetching the first token, and refreshing expired tokens.
     * TODO: Handle failed requests
     */
    public void getTokenAndSave(Map<String, String> requestBody, AccessToken currentToken) throws Exception {

            // 1. Make token request
            Object tokenResponseObject = restTemplate.postForObject(backlog.getTokenUri(), requestBody, Object.class);

            // 2. Parse access token for required data (access token, refresh token, expiry)
            Map<String, Object> tokenResponse = (Map<String, Object>) tokenResponseObject;
            String accessToken = tokenResponse.get("access_token").toString();
            String refreshToken = tokenResponse.get("refresh_token").toString();
            int expiresIn = Integer.valueOf(tokenResponse.get("expires_in").toString());
            Instant expiryTime = Instant.now().plusSeconds(expiresIn - PAD_SECONDS);

            // 3. Fetch backlogUserId if is currently null
            if(currentToken.getUserId() == null){
                String backlogUserId = getUserId(accessToken);
                currentToken.setUserId(backlogUserId);
            }

            // 4. Save token data
            currentToken.refreshData(accessToken, refreshToken, expiryTime);
    }

    public String getUserId(String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate.exchange(
                backlog.getOwnUser(),
                HttpMethod.GET,
                entity,
                String.class);

        String responseString = (String) response.getBody();
        Map<String, Object> userData = (Map<String, Object>) JsonUtil.deserializeJson(responseString);

        return String.valueOf(userData.get("id"));
    }
    
    /*
     * Refresh existing token
     */
    public void refreshToken(AccessToken tokenObject) throws Exception {

        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("grant_type", "refresh_token");
        tokenRequest.put("client_id", backlog.getClientId());
        tokenRequest.put("client_secret", backlog.getClientSecret());
        tokenRequest.put("refresh_token", tokenObject.getRefreshToken());

        getTokenAndSave(tokenRequest, tokenObject);
    }

    /*
     * 1. Fetch Project data from Backlog and update the local data
     * 2. Fetch Issue data from Backlog and update the local data
     */
    public ResponseEntity<?> syncWithBacklog(AccessToken tokenObject) throws Exception {

        String token = tokenObject.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<?> response;

        // 1. Sync Projects
        response = restTemplate.exchange(
                backlog.getProjects(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, String>>>() {});

        List<Map<String, String>> projects = (List<Map<String, String>>) response.getBody();

        List<ProjectIdAndName> idAndNames = projects    // Only "id" and "name" attributes are synchronized
                .stream()
                .map(p -> new ProjectIdAndName(Integer.valueOf(p.get("id")), p.get("name")))
                .collect(Collectors.toList());

        localSync.updateLocalProjects(idAndNames);

        // 2. Sync issues
        response = restTemplate.exchange(
                backlog.getIssues(),
                HttpMethod.GET,
                entity,
                String.class);

        List<Map<String, Object>> issues = // Issue response has nested objects, so JsonUtil is used
            (List<Map<String, Object>>) JsonUtil.deserializeJson((String) response.getBody());

        List<BacklogIssue> backlogIssues = issues
                .stream()
                .map(issue -> { // Convert each response to a BacklogIssue

                    // 1. If the issue is closed or is not assigned to user, it is ireelavent, and hence, skipped.
                    Map<String, String> statusObject = (Map<String, String>) issue.get("status");
                    String statusName = statusObject.get("name");
                    Map<String, String> assigneeObject = (Map<String, String>) issue.get("assignee");
                    String assigneeId = String.valueOf(assigneeObject.get("id"));
                    if (statusName.equals("Closed") || !assigneeId.equals(tokenObject.getUserId())) {
                        return null;
                    }

                    // 2. Extract data from issue response

                    /* issueId    */ Integer id = Integer.valueOf(issue.get("id").toString());
                    /* projectId  */ Integer projectId = Integer.valueOf(issue.get("projectId").toString());
                    /* summary    */ String summary = issue.get("summary").toString();

                    Map<String, Object> priorityObject = (Map<String, Object>) issue.get("priority");
                    Integer priorityId = Integer.valueOf(priorityObject.get("id").toString());
                    /* priority    */ Priority priority = priorityId == 2 ? Priority.HIGH : priorityId == 4 ? Priority.LOW : Priority.MEDIUM;

                    Object estimated = issue.get("estimatedHours");
                    Object actual = issue.get("actualHours");
                    /* remainingHr */ Double remainingHours;
                    try {
                        remainingHours = Double.valueOf(estimated.toString()) - Double.valueOf(actual.toString());
                    } catch (NullPointerException | NumberFormatException e) {
                        remainingHours = 0.0;
                    }

                    // 3. Build and return BacklogIssue
                    return new BacklogIssue(id, projectId, summary, priority, remainingHours);
                })
                .filter(issue -> issue != null) // null objects from irrelevant issues are filtered out
                .collect(Collectors.toList());

            localSync.updateLocalTasks(backlogIssues);

        return ResponseEntity.ok().build();
    }

    public void syncTaskToBacklog(Task task) throws Exception {

        // Return if either backlog or the task is unsynced.
        AccessToken tokenObject = authService.getTokenObject();
        if (tokenObject == null || task.getSyncId() == null) return;

        // If the token has expired, it is refreshed.
        if(tokenObject.isExpired()) refreshToken(tokenObject);

        // Get actualHours
        Double actualHours = taskService.getTotalHoursSpent(task.getProject().getId(), task.getSyncId());

        // Send PATCH request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(tokenObject.getToken());

        String body = "actualHours=" + actualHours;
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        String url = backlog.getIssues() + "/" + task.getSyncId();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    entity,
                    String.class);

            System.out.println("Patched actualHours");
            System.out.println(response.getBody());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
