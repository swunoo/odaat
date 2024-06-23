package com.odaat.odaat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.odaat.odaat.config.BacklogOAuth2Config;
import com.odaat.odaat.model.BacklogAuth;
import com.odaat.odaat.utils.JsonUtil;

@ExtendWith(MockitoExtension.class)
public class BacklogSyncServiceTest {

    @Mock
    private AuthService authService;
    
    @Mock
    private CategoryService categoryService;
    
    @Mock
    private ProjectService projectService;
    
    @Mock
    private TaskService taskService;
    
    @Mock
    private BacklogOAuth2Config backlog;
    
    @Mock
    private LocalSyncService localSync;
    
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JsonUtil jsonUtil;

    @InjectMocks
    private BacklogSyncService backlogSyncService;

    @Test
    public void testGetUserId() throws Exception {

        String token = "testToken";
        String url = "my/url/string";
        String jsonResponse = "{\"id\": 123}";
        Map<String, Object> userData = Map.of("id", 123);

        // Mock the backlog.getOwnUser() to return the specific URL
        when(backlog.getOwnUserApi()).thenReturn(url);

        // Mock the RestTemplate exchange method to return the JSON response
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonResponse);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), eq(entity), eq(String.class)))
                .thenReturn(responseEntity);

        // Mock the JsonUtil.deserializeJson to return the userData map
        MockedStatic<JsonUtil> jsonUtilMock = mockStatic(JsonUtil.class);
        jsonUtilMock.when(() -> JsonUtil.deserializeJson(jsonResponse)).thenReturn(userData);

        // Call the getUserId method
        String userId = backlogSyncService.getUserId(token);

        // Verify the result
        assertEquals("123", userId);
    }

    @Test
    public void testGetTokenAndSave() throws Exception {

        BacklogSyncService mockSync = Mockito.spy(backlogSyncService);

        BacklogAuth currentToken = new BacklogAuth();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "client_credentials");

        Map<String, Object> tokenResponse = new HashMap<>();
        tokenResponse.put("access_token", "accessToken");
        tokenResponse.put("refresh_token", "refreshToken");
        tokenResponse.put("expires_in", 3600);

        when(restTemplate.postForObject(anyString(), any(), any(Class.class))).thenReturn(tokenResponse);
        when(backlog.getTokenApi()).thenReturn("https://api.example.com/oauth/token");

        doAnswer(invocation -> {
            return "123"; // Mocking getUserId method to return "123"
        }).when(mockSync).getUserId(anyString());

        mockSync.getTokenAndSave(requestBody, currentToken);

        verify(restTemplate).postForObject(eq("https://api.example.com/oauth/token"), eq(requestBody), eq(Object.class));
        assertEquals("accessToken", currentToken.getToken());
        assertEquals("refreshToken", currentToken.getRefreshToken());
        assertNotNull(currentToken.getExpiresAt());
        assertEquals("123", currentToken.getUserId());
    }

    @Test
    public void testRefreshToken() throws Exception {
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("grant_type", "refresh_token");
        tokenRequest.put("client_id", "clientId");
        tokenRequest.put("client_secret", "clientSecret");
        tokenRequest.put("refresh_token", "refreshToken");

        BacklogAuth tokenObject = new BacklogAuth();
        tokenObject.setRefreshToken("refreshToken");
        
        BacklogSyncService mockSync = Mockito.spy(backlogSyncService);
        doNothing().when(mockSync).getTokenAndSave(any(), eq(tokenObject));
        when(backlog.getClientId()).thenReturn("clientId");
        when(backlog.getClientSecret()).thenReturn("clientSecret");

        mockSync.refreshToken(tokenObject);

        verify(mockSync).getTokenAndSave(eq(tokenRequest), eq(tokenObject));
    }

    @Test
    public void testSyncWithBacklog() throws Exception {
        BacklogAuth tokenObject = new BacklogAuth();
        tokenObject.setToken("accessToken");
        tokenObject.setUserId("userId");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("accessToken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<Map<String, String>> projectsResponse = List.of(Map.of("id", "1", "name", "Project1"));
        ResponseEntity<List<Map<String, String>>> projectsEntity = ResponseEntity.ok(projectsResponse);

        String issuesResponse = "[{\"id\": \"1\", \"projectId\": \"1\", \"summary\": \"Issue1\", \"status\": {\"name\": \"Open\"}, \"assignee\": {\"id\": \"userId\"}, \"priority\": {\"id\": 2}, \"estimatedHours\": \"10\", \"actualHours\": \"5\"}]";
        ResponseEntity<String> issuesEntity = ResponseEntity.ok(issuesResponse);

        when(backlog.getProjectsApi()).thenReturn("https://api.example.com/projects");
        when(backlog.getIssuesApi()).thenReturn("https://api.example.com/issues");
        when(restTemplate.exchange(eq("https://api.example.com/projects"), eq(HttpMethod.GET), eq(entity), any(ParameterizedTypeReference.class)))
                .thenReturn(projectsEntity);
        when(restTemplate.exchange(eq("https://api.example.com/issues"), eq(HttpMethod.GET), eq(entity), eq(String.class)))
                .thenReturn(issuesEntity);

        backlogSyncService.syncWithBacklog(tokenObject);

        verify(localSync).updateLocalProjects(anyList());
        verify(localSync).updateLocalTasks(anyList());
    }
}