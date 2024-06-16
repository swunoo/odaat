package com.odaat.odaat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.odaat.odaat.config.BacklogOAuth2Config;

@Controller
public class SyncController {

    @Autowired BacklogOAuth2Config backlog;
    

    @GetMapping("/sync/backlog")
    public RedirectView oauth2Backlog(){

        
        // Make request string
        // String request = String.format("%s%s?response_type=code&client_id=%s&redirect_uri=%s&state=%s", backlog.getAppRoot());
        String request = "https://nulab-exam.backlog.jp/OAuth2AccessRequest.action?response_type=code&client_id=PNx4voKbMmAgOFd2TGFEsFFpylEocyAU&redirect_uri=http://localhost:9000/callback&state=state1";

        // Send request
        return new RedirectView(request);
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state){

        System.out.println("CALLBACK");
        System.out.println("code: " + code + " | state: " + state);
        System.out.println("------------------------------");

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", backlog.getClientId());
        tokenRequest.put("client_secret", backlog.getClientSecret());
        tokenRequest.put("code", code);
        tokenRequest.put("redirect_uri", backlog.getRedirectUri());
        tokenRequest.put("grant_type", "authorization_code");

        Map<String, String> tokenResponse = restTemplate.postForObject("https://nulab-exam.backlog.jp/api/v2/oauth2/token ", tokenRequest, Map.class);
        System.out.println("TOKEN");
        System.out.println(tokenResponse);
        System.out.println("------------------------------");
        String accessToken = tokenResponse.get("access_token");

        callProjects(accessToken);

        return accessToken;
    }

    @GetMapping("/tmp")
    public String tmp (){
        callProjects("6nFCCnJOeN1CMIN2AGi4zS8pK8R7OodvA7Yfkz0GifI7l2yjqDk4y3jK664Lx3Tu");
        return "Successful.";
    }

    public void callProjects(String token){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange("https://nulab-exam.backlog.jp/api/v2/projects", HttpMethod.GET, entity, new ParameterizedTypeReference<List<Map<String, String>>>() {});

        System.out.println("PROJECTS");
        System.out.println(response.getBody());
        System.out.println(response.getBody().getClass().getName());
        System.out.println("------------------------------");

    }

}
