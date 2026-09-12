package com.project.demo.easydl;

import com.project.demo.config.AppSecurityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class EasyDlTokenServiceTest {

    private RedisTemplate<String, Object> redis;
    private ValueOperations<String, Object> values;
    private RestTemplate restTemplate;
    private AppSecurityProperties properties;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        redis = mock(RedisTemplate.class);
        values = mock(ValueOperations.class);
        restTemplate = mock(RestTemplate.class);
        properties = enabledProperties();
        when(redis.opsForValue()).thenReturn(values);
    }

    @Test
    void reusesCachedAccessToken() {
        when(values.get("easydl:access-token")).thenReturn("cached-token");

        EasyDlTokenService service = new EasyDlTokenService(redis, restTemplate, properties);

        assertEquals("cached-token", service.getToken());
        verifyNoInteractions(restTemplate);
    }

    @Test
    void fetchesAndCachesTokenWithoutReturningUpstreamMetadata() {
        when(values.get("easydl:access-token")).thenReturn(null);
        Map<String, Object> response = new HashMap<>();
        response.put("access_token", "new-token");
        response.put("expires_in", 2592000);
        when(restTemplate.postForObject(any(URI.class), any(), org.mockito.ArgumentMatchers.eq(Map.class)))
                .thenReturn(response);

        EasyDlTokenService service = new EasyDlTokenService(redis, restTemplate, properties);

        assertEquals("new-token", service.getToken());
        verify(values).set("easydl:access-token", "new-token", Duration.ofSeconds(2591700));
    }

    @Test
    void refusesToCallUpstreamWhenIntegrationIsDisabled() {
        properties.getEasyDl().setEnabled(false);

        EasyDlTokenService service = new EasyDlTokenService(redis, restTemplate, properties);

        assertThrows(EasyDlUnavailableException.class, service::getToken);
        verify(values, never()).get(any());
        verifyNoInteractions(restTemplate);
    }

    private AppSecurityProperties enabledProperties() {
        AppSecurityProperties config = new AppSecurityProperties();
        config.getEasyDl().setEnabled(true);
        config.getEasyDl().setApiKey("FAKE_API_KEY");
        config.getEasyDl().setSecretKey("FAKE_SECRET_KEY");
        config.getEasyDl().setTokenUrl("https://aip.baidubce.com/oauth/2.0/token");
        config.getEasyDl().setModelUrl(
                "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/FAKE_MODEL");
        return config;
    }
}
