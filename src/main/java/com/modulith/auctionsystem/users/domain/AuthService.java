package com.modulith.auctionsystem.users.domain;

import com.modulith.auctionsystem.users.domain.models.LoginRequest;
import com.modulith.auctionsystem.users.domain.models.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Value("${keycloak.token-uri:http://localhost:8180/realms/auction-system/protocol/openid-connect/token}")
    private String keycloakTokenUri;

    @Value("${keycloak.client-id:auction-api}")
    private String clientId;

    @Value("${keycloak.client-secret:}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public LoginResponse login(LoginRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "password");
            body.add("client_id", clientId);
            body.add("username", request.getUsername());
            body.add("password", request.getPassword());
            body.add("scope", "openid");

            // Add client_secret if it's configured (for confidential clients)
            if (clientSecret != null && !clientSecret.isEmpty()) {
                body.add("client_secret", clientSecret);
            }

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

            log.debug("Attempting to authenticate user: {}", request.getUsername());

            ResponseEntity<LoginResponse> response = restTemplate.exchange(
                    keycloakTokenUri,
                    HttpMethod.POST,
                    requestEntity,
                    LoginResponse.class
            );

            log.info("User {} authenticated successfully", request.getUsername());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            log.error("Authentication failed for user {}: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("Invalid username or password", e);
        } catch (Exception e) {
            log.error("Error during authentication for user {}: {}", request.getUsername(), e.getMessage());
            throw new RuntimeException("Authentication service error", e);
        }
    }
}

