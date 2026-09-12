package com.project.demo.easydl;

import com.project.demo.config.AppSecurityProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class EasyDlClient {

    private final EasyDlTokenService tokenService;
    private final RestTemplate restTemplate;
    private final AppSecurityProperties.EasyDl config;

    public EasyDlClient(EasyDlTokenService tokenService,
                        @Qualifier("easyDlRestTemplate") RestTemplate restTemplate,
                        AppSecurityProperties properties) {
        this.tokenService = tokenService;
        this.restTemplate = restTemplate;
        this.config = properties.getEasyDl();
    }

    public DiagnosisResult diagnose(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("Image bytes are required");
        }
        String token = tokenService.getToken();
        Map<String, Object> response = callModel(imageBytes, token);
        if (isExpiredToken(response)) {
            tokenService.invalidate(token);
            response = callModel(imageBytes, tokenService.getToken());
        }
        if (containsUpstreamError(response)) {
            throw new EasyDlUnavailableException("EasyDL model returned an error");
        }
        return normalize(response);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> callModel(byte[] imageBytes, String token) {
        URI uri = UriComponentsBuilder.fromHttpUrl(config.getModelUrl())
                .replaceQueryParam("access_token", token)
                .build(true)
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> request = Collections.singletonMap(
                "image", Base64.getEncoder().encodeToString(imageBytes));
        try {
            Map<String, Object> response = restTemplate.postForObject(
                    uri, new HttpEntity<>(request, headers), Map.class);
            if (response == null) {
                throw new EasyDlUnavailableException("EasyDL model returned an empty response");
            }
            return response;
        } catch (EasyDlUnavailableException exception) {
            throw exception;
        } catch (RestClientException | IllegalArgumentException exception) {
            throw new EasyDlUnavailableException("EasyDL model endpoint is unavailable", exception);
        }
    }

    private boolean isExpiredToken(Map<String, Object> response) {
        long errorCode = asLong(response.get("error_code"));
        return errorCode == 110L || errorCode == 111L;
    }

    private boolean containsUpstreamError(Map<String, Object> response) {
        return response.containsKey("error_code") || response.containsKey("error_msg");
    }

    @SuppressWarnings("unchecked")
    private DiagnosisResult normalize(Map<String, Object> response) {
        Object rawResults = response.get("results");
        if (!(rawResults instanceof List)) {
            throw new EasyDlUnavailableException("EasyDL result is malformed");
        }
        List<?> results = (List<?>) rawResults;
        return results.stream()
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(this::toDiagnosisResult)
                .filter(result -> result != null)
                .max(Comparator.comparingDouble(DiagnosisResult::getScore))
                .orElseThrow(() -> new EasyDlUnavailableException("EasyDL result is empty"));
    }

    private DiagnosisResult toDiagnosisResult(Map<?, ?> value) {
        Object rawName = value.get("name");
        Object rawScore = value.get("score");
        if (!(rawName instanceof String) || !StringUtils.hasText((String) rawName)) {
            return null;
        }
        double score = asDouble(rawScore);
        if (!Double.isFinite(score) || score < 0.0d || score > 1.0d) {
            return null;
        }
        String name = ((String) rawName).trim();
        if (name.length() > 120) {
            name = name.substring(0, 120);
        }
        return new DiagnosisResult(name, score);
    }

    private long asLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        try {
            return value == null ? 0L : Long.parseLong(value.toString());
        } catch (NumberFormatException exception) {
            return 0L;
        }
    }

    private double asDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        try {
            return value == null ? Double.NaN : Double.parseDouble(value.toString());
        } catch (NumberFormatException exception) {
            return Double.NaN;
        }
    }
}
