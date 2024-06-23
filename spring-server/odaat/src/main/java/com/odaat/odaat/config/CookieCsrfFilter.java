package com.odaat.odaat.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

 /*
   Spring Security 6 doesn't set a XSRF-TOKEN cookie by default.
   So, it is added in this filter.
   Ref: https://github.com/spring-projects/spring-security/issues/12141#issuecomment-1321345077
  */
public class CookieCsrfFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    response.setHeader(csrfToken.getHeaderName(), csrfToken.getToken());
    filterChain.doFilter(request, response);
  }
}