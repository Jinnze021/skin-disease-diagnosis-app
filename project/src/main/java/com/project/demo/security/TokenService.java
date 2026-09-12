package com.project.demo.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

@Service
public class TokenService {

    public static final String TOKEN_HEADER = "x-auth-token";
    private static final Duration SESSION_TTL = Duration.ofHours(2);
    private static final String KEY_PREFIX = "session:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final SecureRandom secureRandom;

    public TokenService(RedisTemplate<String, Object> redisTemplate) {
        this(redisTemplate, new SecureRandom());
    }

    TokenService(RedisTemplate<String, Object> redisTemplate, SecureRandom secureRandom) {
        this.redisTemplate = redisTemplate;
        this.secureRandom = secureRandom;
    }

    public IssuedToken issue(SessionUser user) {
        if (user == null || user.getUserId() == null) {
            throw new IllegalArgumentException("Session user is required");
        }
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        redisTemplate.opsForValue().set(redisKey(rawToken), user, SESSION_TTL);
        return new IssuedToken(rawToken, SESSION_TTL.getSeconds());
    }

    public Optional<SessionUser> resolve(String rawToken) {
        if (!StringUtils.hasText(rawToken)) {
            return Optional.empty();
        }
        Object stored = redisTemplate.opsForValue().get(redisKey(rawToken));
        if (!(stored instanceof SessionUser)) {
            return Optional.empty();
        }
        return Optional.of((SessionUser) stored);
    }

    public void revoke(String rawToken) {
        if (StringUtils.hasText(rawToken)) {
            redisTemplate.delete(redisKey(rawToken));
        }
    }

    private String redisKey(String rawToken) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(rawToken.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(digest.length * 2);
            for (byte value : digest) {
                hex.append(String.format("%02x", value & 0xff));
            }
            return KEY_PREFIX + hex;
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
