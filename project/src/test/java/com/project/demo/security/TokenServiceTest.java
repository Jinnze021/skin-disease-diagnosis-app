package com.project.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenServiceTest {

    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private TokenService tokenService;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        tokenService = new TokenService(redisTemplate);
    }

    @Test
    void issuesOpaqueTokenAndStoresOnlyItsDigestWithTwoHourTtl() {
        SessionUser sessionUser = new SessionUser(7, "注册用户");

        IssuedToken issued = tokenService.issue(sessionUser);

        assertTrue(issued.getToken().matches("[A-Za-z0-9_-]{43}"));
        assertEquals(7200L, issued.getExpiresInSeconds());

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        verify(valueOperations).set(keyCaptor.capture(), eq(sessionUser), eq(Duration.ofHours(2)));
        String storedKey = keyCaptor.getValue();
        assertTrue(storedKey.matches("session:[0-9a-f]{64}"));
        assertFalse(storedKey.contains(issued.getToken()));
    }

    @Test
    void resolvesAndRevokesUsingTheSameHashedRedisKey() throws Exception {
        String rawToken = "raw-token-that-must-never-be-used-as-a-redis-key";
        SessionUser sessionUser = new SessionUser(9, "管理员");
        when(valueOperations.get(expectedKey(rawToken))).thenReturn(sessionUser);

        Optional<SessionUser> resolved = tokenService.resolve(rawToken);
        tokenService.revoke(rawToken);

        assertTrue(resolved.isPresent());
        assertSame(sessionUser, resolved.get());
        verify(valueOperations).get(expectedKey(rawToken));
        verify(redisTemplate).delete(expectedKey(rawToken));
    }

    @Test
    void rejectsBlankOrUnexpectedSessionValues() {
        assertFalse(tokenService.resolve(null).isPresent());
        assertFalse(tokenService.resolve("  ").isPresent());

        when(valueOperations.get(expectedKeyUnchecked("valid-looking-token"))).thenReturn("not-a-session");
        assertFalse(tokenService.resolve("valid-looking-token").isPresent());
    }

    private static String expectedKey(String token) throws Exception {
        return "session:" + toHex(MessageDigest.getInstance("SHA-256")
                .digest(token.getBytes(StandardCharsets.UTF_8)));
    }

    private static String expectedKeyUnchecked(String token) {
        try {
            return expectedKey(token);
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            builder.append(String.format("%02x", value & 0xff));
        }
        return builder.toString();
    }
}
