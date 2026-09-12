package com.project.demo.easydl;

import com.project.demo.config.AppSecurityProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EasyDlClientTest {

    private EasyDlTokenService tokenService;
    private RestTemplate restTemplate;
    private AppSecurityProperties properties;

    @BeforeEach
    void setUp() {
        tokenService = mock(EasyDlTokenService.class);
        restTemplate = mock(RestTemplate.class);
        properties = new AppSecurityProperties();
        properties.getEasyDl().setEnabled(true);
        properties.getEasyDl().setModelUrl(
                "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/FAKE_MODEL");
    }

    @Test
    void normalizesHighestScoringResult() {
        when(tokenService.getToken()).thenReturn("FAKE_TOKEN");
        Map<String, Object> low = result("psoriasis", 0.23d);
        Map<String, Object> high = result("eczema", 0.91d);
        Map<String, Object> upstream = new HashMap<>();
        upstream.put("results", Arrays.asList(low, high));
        when(restTemplate.postForObject(any(URI.class), any(), eq(Map.class))).thenReturn(upstream);

        DiagnosisResult result = new EasyDlClient(tokenService, restTemplate, properties)
                .diagnose(new byte[]{1, 2, 3});

        assertEquals("eczema", result.getName());
        assertEquals(0.91d, result.getScore(), 0.0001d);
    }

    @Test
    void invalidatesExpiredTokenAndRetriesExactlyOnce() {
        when(tokenService.getToken()).thenReturn("FAKE_OLD_TOKEN", "FAKE_NEW_TOKEN");
        Map<String, Object> expired = new HashMap<>();
        expired.put("error_code", 110);
        Map<String, Object> success = new HashMap<>();
        success.put("results", Collections.singletonList(result("eczema", 0.88d)));
        when(restTemplate.postForObject(any(URI.class), any(), eq(Map.class)))
                .thenReturn(expired, success);

        DiagnosisResult result = new EasyDlClient(tokenService, restTemplate, properties)
                .diagnose(new byte[]{1});

        assertEquals("eczema", result.getName());
        verify(tokenService).invalidate("FAKE_OLD_TOKEN");
        verify(restTemplate, times(2)).postForObject(any(URI.class), any(), eq(Map.class));
    }

    @Test
    void rejectsEmptyOrMalformedUpstreamResults() {
        when(tokenService.getToken()).thenReturn("FAKE_TOKEN");
        when(restTemplate.postForObject(any(URI.class), any(), eq(Map.class)))
                .thenReturn(Collections.singletonMap("results", Collections.emptyList()));

        EasyDlClient client = new EasyDlClient(tokenService, restTemplate, properties);

        assertThrows(EasyDlUnavailableException.class, () -> client.diagnose(new byte[]{1}));
    }

    private Map<String, Object> result(String name, double score) {
        Map<String, Object> value = new HashMap<>();
        value.put("name", name);
        value.put("score", score);
        return value;
    }
}
