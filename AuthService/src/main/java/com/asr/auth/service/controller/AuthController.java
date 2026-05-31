package com.asr.auth.service.controller;

import com.asr.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register-client")
    public ResponseEntity<Map<String, String>> registerNewClient(@RequestParam String clientName) {
        Map<String, String> clientCredentials = authService.createDynamicClient(clientName);
        return ResponseEntity.ok(clientCredentials);
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getAccessToken(
            @RequestParam String clientId,
            @RequestParam String clientSecret) {
        Map<String, Object> tokenResponse = authService.loginWithClientCredentials(clientId, clientSecret);
        return ResponseEntity.ok(tokenResponse);
    }
}
