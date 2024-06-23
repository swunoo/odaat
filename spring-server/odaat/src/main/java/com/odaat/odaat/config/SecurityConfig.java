package com.odaat.odaat.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SimpleSavedRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
  ----------------------------------
   I. SecurityFilterChain
  ----------------------------------
  1. Requests
  - All OPTIONS requests are permitted to allow preflight requests.
  - Requests to '/api/user' and '/api/callback' are permitted.
  - '/api/user' is used to check if a user is authenticated.
  - '/api/callback' is used to allow Backlog to send the access token.
  
  2. Filters
  - CookieCsrfFilter is used to append XSRF-TOKEN cookie.
  - SpaWebFilter is used to handle redirects to the SPA web application.
  
  3. Login
  - OAuth2 login is used with an external provider (auth0).
  
  ----------------------------------
   II. CorsConfigurationSource
  ----------------------------------
  - The origin of client web application is allowed.
  - All headers and methods are allowed.
  
  ----------------------------------
   III. RequestCache
  ----------------------------------
  - Request referer is used to redirect to the client after logging in.
  - Built-in mechanism is leveraged by using "SPRING_SECURITY_SAVED_REQUEST".
  
  ----------------------------------
   IV. References
  ----------------------------------
  - https://auth0.com/blog/simple-crud-react-and-spring-boot/
  - https://stackoverflow.com/questions/631217/spring-security-how-to-get-the-initial-target-url
 */
@Configuration
public class SecurityConfig {

    @Value("${client.url}")
    private String clientApplicationUrl;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests((authz) -> authz
                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                .requestMatchers("/api/user", "/api/callback").permitAll()
                .anyRequest().authenticated()
            )
            .csrf((csrf) -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            )
            .addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
            .oauth2Login(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of(clientApplicationUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public RequestCache refererRequestCache() { 
        return new HttpSessionRequestCache() {
            @Override
            public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
                String referrer = request.getHeader("referer");
                if (referrer == null) {
                    referrer = request.getRequestURL().toString();
                }
                request.getSession().setAttribute("SPRING_SECURITY_SAVED_REQUEST",
                    new SimpleSavedRequest(referrer));

            }
        };
    }
}