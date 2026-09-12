package com.project.demo.easydl;

import com.project.demo.config.AppSecurityProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

@Service
public class EasyDlTokenService {

    private static final String CACHE_KEY = "easydl:access-token";
    private static final long EXPIRY_SAFETY_SECONDS = 300L;
    private static final long MIN_CACHE_SECONDS = 60L;

    private final RedisTemplate<String, Object> redis;
    private final RestTemplate restTemplate;
    private final AppSecurityProperties.EasyDl config;

    public EasyDlTokenService(RedisTemplate<String, Object> redis,
                              @Qualifier("easyDlRestTemplate") RestTemplate restTemplate,
                              AppSecurityProperties properties) {
        this.redis = redis;
        this.restTemplate = restTemplate;
        this.config = properties.getEasyDl();
    }

    public String getToken() {
        requireEnabled();
        Object cached = redis.opsForValue().get(CACHE_KEY);
        if (cached instanceof String && StringUtils.hasText((String) cached)) {
            return (String) cached;
        }
        return fetchAndCacheToken();
    }

    public void invalidate(String expectedToken) {
        if (!StringUtils.hasText(expectedToken)) {
            return;
        }
        Object cached = redis.opsForValue().get(CACHE_KEY);
        if (expectedToken.equals(cached)) {
            redis.delete(CACHE_KEY);
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized String fetchAndCacheToken() {
        Object cached = redis.opsForValue().get(CACHE_KEY);
        if (cached instanceof String && StringUtils.hasText((String) cached)) {
            return (String) cached;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", config.getApiKey());
        form.add("client_secret", config.getSecretKey());

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    URI.create(config.getTokenUrl()), new HttpEntity<>(form, headers), Map.class);
            String token = response == null ? null : asString(response.get("access_token"));
            long expiresIn = response == null ? 0L : asLong(response.get("expires_in"));
            if (!StringUtils.hasText(token) || expiresIn <= EXPIRY_SAFETY_SECONDS) {
                throw new EasyDlUnavailableException("EasyDL token response is invalid");
            }
            long cacheSeconds = Math.max(MIN_CACHE_SECONDS, expiresIn - EXPIRY_SAFETY_SECONDS);
            redis.opsForValue().set(CACHE_KEY, token, Duration.ofSeconds(cacheSeconds));
            return token;
        } catch (EasyDlUnavailableException exception) {
            throw exception;
        } catch (RestClientException | IllegalArgumentException exception) {
            throw new EasyDlUnavailableException("EasyDL token endpoint is unavailable", exception);
        }
    }

    private void requireEnabled() {
        if (!config.isEnabled()) {
            throw new EasyDlDisabledException();
        }
    }

    private String asString(Object value) {
        return value instanceof String ? (String) value : null;
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
}
