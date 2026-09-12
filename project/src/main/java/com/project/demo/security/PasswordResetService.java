package com.project.demo.security;

import com.project.demo.config.AppSecurityProperties;
import com.project.demo.entity.User;
import com.project.demo.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Duration;
import java.util.Locale;

@Service
public class PasswordResetService {

    private static final String PURPOSE = "password-reset";
    private static final Duration CODE_TTL = Duration.ofMinutes(10);
    private static final Duration SEND_COOLDOWN = Duration.ofSeconds(60);
    private static final Duration RATE_WINDOW = Duration.ofHours(1);
    private static final int MAX_ATTEMPTS = 5;
    private static final long MAX_REQUESTS_PER_HOUR = 5L;

    private final RedisTemplate<String, Object> redisTemplate;
    private final JavaMailSender mailSender;
    private final UserService userService;
    private final PasswordService passwordService;
    private final VerificationCodeGenerator codeGenerator;
    private final AppSecurityProperties properties;
    private final Clock clock;

    public PasswordResetService(RedisTemplate<String, Object> redisTemplate,
                                JavaMailSender mailSender,
                                UserService userService,
                                PasswordService passwordService,
                                VerificationCodeGenerator codeGenerator,
                                AppSecurityProperties properties) {
        this(redisTemplate, mailSender, userService, passwordService, codeGenerator,
                properties, Clock.systemUTC());
    }

    PasswordResetService(RedisTemplate<String, Object> redisTemplate,
                         JavaMailSender mailSender,
                         UserService userService,
                         PasswordService passwordService,
                         VerificationCodeGenerator codeGenerator,
                         AppSecurityProperties properties,
                         Clock clock) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
        this.userService = userService;
        this.passwordService = passwordService;
        this.codeGenerator = codeGenerator;
        this.properties = properties;
        this.clock = clock;
    }

    public void request(String email, String sourceKey) {
        ensureEnabled();
        String normalizedEmail = normalizeEmail(email);
        if (!isValidEmail(normalizedEmail)) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }

        enforceRequestLimits(normalizedEmail, sourceKey);

        String code = codeGenerator.generate();
        long expiresAt = clock.millis() + CODE_TTL.toMillis();
        ResetRecord record = new ResetRecord(normalizedEmail, PURPOSE,
                hmac(normalizedEmail, code), MAX_ATTEMPTS, expiresAt);
        String resetKey = resetKey(normalizedEmail);
        redisTemplate.opsForValue().set(resetKey, record, CODE_TTL);

        User user = userService.findByEmail(normalizedEmail);
        if (user == null) {
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(normalizedEmail);
            message.setSubject("皮肤病智诊影析 - 密码重置验证码");
            message.setText("您的密码重置验证码为：" + code + "。验证码10分钟内有效，请勿转发给他人。");
            mailSender.send(message);
        } catch (RuntimeException exception) {
            redisTemplate.delete(resetKey);
            throw new IllegalStateException("验证码发送失败，请稍后重试", exception);
        }
    }

    public void confirm(String email, String code, String newPassword) {
        ensureEnabled();
        String normalizedEmail = normalizeEmail(email);
        if (!StringUtils.hasText(normalizedEmail) || !StringUtils.hasText(code)) {
            throw new InvalidResetCodeException();
        }

        String key = resetKey(normalizedEmail);
        Object stored = redisTemplate.opsForValue().getAndDelete(key);
        if (!(stored instanceof ResetRecord)) {
            throw new InvalidResetCodeException();
        }

        ResetRecord record = (ResetRecord) stored;
        long remainingMillis = record.expiresAt - clock.millis();
        if (remainingMillis <= 0 || !PURPOSE.equals(record.purpose)
                || !normalizedEmail.equals(record.email)) {
            throw new InvalidResetCodeException();
        }

        byte[] expected = record.codeDigest.getBytes(StandardCharsets.US_ASCII);
        byte[] supplied = hmac(normalizedEmail, code).getBytes(StandardCharsets.US_ASCII);
        if (!MessageDigest.isEqual(expected, supplied)) {
            int remainingAttempts = record.remainingAttempts - 1;
            if (remainingAttempts > 0) {
                redisTemplate.opsForValue().set(key, record.withAttempts(remainingAttempts),
                        Duration.ofMillis(remainingMillis));
            }
            throw new InvalidResetCodeException();
        }

        User user = userService.findByEmail(normalizedEmail);
        if (user == null) {
            throw new InvalidResetCodeException();
        }
        String encodedPassword = passwordService.hash(newPassword);
        if (!userService.updatePassword(user.getUserId(), encodedPassword)) {
            throw new IllegalStateException("密码更新失败");
        }
    }

    private void enforceRequestLimits(String email, String sourceKey) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        String cooldownKey = "password-reset:cooldown:" + sha256(email);
        if (!Boolean.TRUE.equals(values.setIfAbsent(cooldownKey, "1", SEND_COOLDOWN))) {
            throw new IllegalStateException("请求过于频繁，请稍后再试");
        }

        String normalizedSource = StringUtils.hasText(sourceKey) ? sourceKey.trim() : "unknown";
        String rateKey = "password-reset:rate:" + sha256(normalizedSource);
        Long count = values.increment(rateKey);
        if (count != null && count == 1L) {
            redisTemplate.expire(rateKey, RATE_WINDOW);
        }
        if (count == null || count > MAX_REQUESTS_PER_HOUR) {
            throw new IllegalStateException("请求过于频繁，请稍后再试");
        }
    }

    private void ensureEnabled() {
        if (!properties.getPasswordReset().isEnabled()
                || !StringUtils.hasText(properties.getPasswordReset().getHmacKey())) {
            throw new IllegalStateException("密码找回服务未启用");
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    private boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email) || !email.contains("@")) {
            return false;
        }
        try {
            InternetAddress address = new InternetAddress(email, true);
            return email.equals(address.getAddress());
        } catch (AddressException exception) {
            return false;
        }
    }

    private String resetKey(String email) {
        return "password-reset:code:" + sha256(email);
    }

    private String hmac(String email, String code) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(properties.getPasswordReset().getHmacKey()
                    .getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return toHex(mac.doFinal((PURPOSE + ":" + email + ":" + code)
                    .getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("HMAC-SHA-256 is unavailable", exception);
        }
    }

    private String sha256(String value) {
        try {
            return toHex(MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            builder.append(String.format(Locale.ROOT, "%02x", value & 0xff));
        }
        return builder.toString();
    }

    private static final class ResetRecord implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String email;
        private final String purpose;
        private final String codeDigest;
        private final int remainingAttempts;
        private final long expiresAt;

        private ResetRecord(String email, String purpose, String codeDigest,
                            int remainingAttempts, long expiresAt) {
            this.email = email;
            this.purpose = purpose;
            this.codeDigest = codeDigest;
            this.remainingAttempts = remainingAttempts;
            this.expiresAt = expiresAt;
        }

        private ResetRecord withAttempts(int attempts) {
            return new ResetRecord(email, purpose, codeDigest, attempts, expiresAt);
        }
    }
}
