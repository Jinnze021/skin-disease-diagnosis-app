package com.project.demo.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Locale;

@Component
public class VerificationCodeGenerator {

    private final SecureRandom secureRandom;

    public VerificationCodeGenerator() {
        this(new SecureRandom());
    }

    VerificationCodeGenerator(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public String generate() {
        return String.format(Locale.ROOT, "%06d", secureRandom.nextInt(1_000_000));
    }
}
