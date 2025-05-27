package org.example.codesignconnect.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.codesignconnect.service.OAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OAuthServiceImpl implements OAuthService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${miro.client-id}")
    private String clientId;
    @Value("${miro.client-secret}")
    private String clientSecret;

    @Override
    public String refreshMiroToken() {
        String url = "https://api.miro.com/v1/oauth/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("refresh_token", "eyJtaXJvLm9yaWdpbiI6ImV1MDEifQ__itpzarJLr9psBF3MLwoLTuxzdk");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }
}
