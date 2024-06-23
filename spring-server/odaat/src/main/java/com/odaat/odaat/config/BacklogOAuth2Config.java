package com.odaat.odaat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class BacklogOAuth2Config {
    @Value("${backlog.app.root}")
    private String appRoot;

    @Value("${backlog.oauth2.client-id}")
    private String clientId;

    @Value("${backlog.oauth2.client-secret}")
    private String clientSecret;

    @Value("${backlog.oauth2.authorization-uri}")
    private String authorizationUri;

    @Value("${backlog.oauth2.redirect-uri}")
    private String redirectUri;

    /*
      Backlog APIs used in this project.
      Ref: https://developer.nulab.com/docs/backlog/
     */
    private String tokenUri = appRoot + "/api/v2/oauth2/token";
    private String projects = appRoot + "/api/v2/projects";
    private String issues = appRoot + "/api/v2/issues";
    private String ownUser = appRoot + "/api/v2/users/myself";

}
