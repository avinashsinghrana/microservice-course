package com.lcwd.rating.config;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtRequestFilter implements Filter {

    private static final String MDC_USER_KEY = "userId";
    private static final String MDC_CLIENT_KEY = "clientId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String userId = jwtAuth.getToken().getClaimAsString("preferred_username");
            if (userId == null) {
                userId = jwtAuth.getName(); // Fallback to 'sub' claim
            }

            String clientId = jwtAuth.getToken().getClaimAsString("azp");

            MDC.put(MDC_USER_KEY, userId);
            if (clientId != null) {
                MDC.put(MDC_CLIENT_KEY, clientId);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_USER_KEY);
            MDC.remove(MDC_CLIENT_KEY);
        }
    }
}