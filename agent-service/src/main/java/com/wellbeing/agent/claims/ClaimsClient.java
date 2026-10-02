package com.wellbeing.agent.claims;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Calls mock-claims-service. Plumbing only - the agent's tools use this.
 * Returns null when the thing isn't found, so callers must handle null.
 *
 * Base URL: defaults to http://localhost:8081. If your claims service actually
 * started on a different port (yours came up on 8080 earlier), set this in
 * application.yml:
 *
 *   claims:
 *     base-url: http://localhost:8080
 */
@Component
public class ClaimsClient {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    public ClaimsClient(@Value("${claims.base-url:http://localhost:8081}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public Map<String, Object> getClaim(String claimId) {
        try {
            return restClient.get()
                    .uri("/api/claims/{id}", claimId)
                    .retrieve()
                    .body(MAP_TYPE);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    public Map<String, Object> getPlan(String employeeId) {
        try {
            return restClient.get()
                    .uri("/api/plans/{employeeId}", employeeId)
                    .retrieve()
                    .body(MAP_TYPE);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }

    public Map<String, Object> checkEligibility(String employeeId, String program) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/eligibility")
                        .queryParam("employeeId", employeeId)
                        .queryParam("program", program)
                        .build())
                .retrieve()
                .body(MAP_TYPE);
    }
}
