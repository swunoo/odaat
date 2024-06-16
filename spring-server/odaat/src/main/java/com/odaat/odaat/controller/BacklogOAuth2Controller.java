package com.odaat.odaat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class BacklogOAuth2Controller {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping("/oauth2/authorize")
    public RedirectView authorize() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("external");
        String authorizationRequestUri = clientRegistration.getProviderDetails().getAuthorizationUri() +
                "?response_type=code&client_id=" + clientRegistration.getClientId() +
                "&redirect_uri=" + clientRegistration.getRedirectUri() +
                "&scope=read&state=state";

        return new RedirectView(authorizationRequestUri);
    }

    @GetMapping("/oauth2/callback")
    public String callback(@RequestParam("code") String code, @AuthenticationPrincipal Authentication principal) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("external");

        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("client_id", clientRegistration.getClientId());
        tokenRequest.put("client_secret", clientRegistration.getClientSecret());
        tokenRequest.put("code", code);
        tokenRequest.put("redirect_uri", clientRegistration.getRedirectUri());
        tokenRequest.put("grant_type", "authorization_code");

        Map<String, String> tokenResponse = restTemplate.postForObject(clientRegistration.getProviderDetails().getTokenUri(), tokenRequest, Map.class);
        String accessToken = tokenResponse.get("access_token");

        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, null, null);
        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(clientRegistration, principal.getName(), oAuth2AccessToken);

        authorizedClientService.saveAuthorizedClient(authorizedClient, principal);

        return "redirect:/some-protected-resource";
    }

    @GetMapping("/some-protected-resource")
    public String callProtectedResource(@AuthenticationPrincipal Principal principal) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("external", principal.getName());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("https://api.external-service.com/resource", HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
