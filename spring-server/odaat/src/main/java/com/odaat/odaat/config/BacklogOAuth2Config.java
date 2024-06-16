package com.odaat.odaat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;


public class BacklogOAuth2Config {
    @Value("${backlog.oauth2.client-id}")
    private String clientId;

    @Value("${backlog.oauth2.client-secret}")
    private String clientSecret;

    @Value("${backlog.oauth2.authorization-uri}")
    private String authorizationUri;

    @Value("${backlog.oauth2.token-uri}")
    private String tokenUri;

    @Value("${backlog.oauth2.redirect-uri}")
    private String redirectUri;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("external")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .redirectUri(redirectUri)
                .scope("read", "write")
                .build();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}
