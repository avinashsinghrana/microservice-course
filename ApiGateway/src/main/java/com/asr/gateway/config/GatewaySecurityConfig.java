package com.asr.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Public endpoints (Auth Service login/registration)
                        .requestMatchers("/auth-service/**").permitAll()

                        // 2. Simply require ANY valid authenticated JWT token for microservices.
                        // No role checks or scope checks happen here anymore!
                        .anyRequest().authenticated()
                )
                // Validates the cryptographic signature against Keycloak automatically
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}