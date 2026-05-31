package com.asr.auth.service.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${keycloak.auth-server-url}") private String authServerUrl;
    @Value("${keycloak.realm}") private String realm;
    @Value("${keycloak.client-id}") private String masterClientId;
    @Value("${keycloak.client-secret}") private String masterClientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    // Method to provision a client dynamically
    public Map<String, String> createDynamicClient(String clientName) {
        // Initialize Admin Client using the Gateway's Token Identity
        Keycloak keycloakAdmin = KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType("client_credentials")
                .clientId(masterClientId)
                .clientSecret(masterClientSecret)
                .build();

        String generatedClientId = clientName.toLowerCase().replaceAll("\\s+", "-") + "-" + UUID.randomUUID().toString().substring(0, 5);

        // Define the new client configuration
        ClientRepresentation newClient = new ClientRepresentation();
        newClient.setClientId(generatedClientId);
        newClient.setName(clientName);
        newClient.setEnabled(true);
        newClient.setServiceAccountsEnabled(true); // Enables Client Credentials flow
        newClient.setPublicClient(false);          // Makes it confidential
        newClient.setDirectAccessGrantsEnabled(false);

        Response response = keycloakAdmin.realm(realm).clients().create(newClient);

        if (response.getStatus() == 201) {
            // Retrieve the generated secret from the Keycloak backchannel
            String id = keycloakAdmin.realm(realm).clients().findByClientId(generatedClientId).get(0).getId();
            String generatedSecret = keycloakAdmin.realm(realm).clients().get(id).getSecret().getValue();

            Map<String, String> credentials = new HashMap<>();
            credentials.put("clientId", generatedClientId);
            credentials.put("clientSecret", generatedSecret);
            return credentials;
        }

        throw new RuntimeException("Failed to generate dynamic client profile: Status " + response.getStatus());
    }

    // Method to request JWT using Client Credentials flow
    public Map<String, Object> loginWithClientCredentials(String clientId, String clientSecret) {
        String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

        return response.getBody();
    }
}