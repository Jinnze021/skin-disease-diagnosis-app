package com.project.demo.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Service
public class PasswordService {

    private static final int MIN_PASSWORD_CHARACTERS = 8;
    private static final int MAX_BCRYPT_BYTES = 72;
    private static final Pattern BCRYPT_PATTERN = Pattern.compile(
            "^\\$2[ayb]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String hash(String rawPassword) {
        validate(rawPassword);
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        if (!isValid(rawPassword) || encodedPassword == null
                || !BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            return false;
        }
        try {
            return encoder.matches(rawPassword, encodedPassword);
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private void validate(String rawPassword) {
        if (!isValid(rawPassword)) {
            throw new IllegalArgumentException("Password must contain 8 to 72 UTF-8 bytes and cannot be blank");
        }
    }

    private boolean isValid(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            return false;
        }
        if (rawPassword.length() < MIN_PASSWORD_CHARACTERS) {
            return false;
        }
        return rawPassword.getBytes(StandardCharsets.UTF_8).length <= MAX_BCRYPT_BYTES;
    }
}
