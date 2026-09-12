package com.project.demo.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppSecurityPropertiesTest {

    @Test
    void rejectsBlankEasyDlSecretsWhenDiagnosisIsEnabled() {
        AppSecurityProperties properties = new AppSecurityProperties();
        properties.getEasyDl().setEnabled(true);

        assertThrows(IllegalStateException.class, properties::validate);
    }

    @Test
    void rejectsBlankHmacKeyWhenPasswordResetIsEnabled() {
        AppSecurityProperties properties = new AppSecurityProperties();
        properties.getPasswordReset().setEnabled(true);

        assertThrows(IllegalStateException.class, properties::validate);
    }

    @Test
    void rejectsShortHmacKeyWhenPasswordResetIsEnabled() {
        AppSecurityProperties properties = new AppSecurityProperties();
        properties.getPasswordReset().setEnabled(true);
        properties.getPasswordReset().setHmacKey("too-short");

        assertThrows(IllegalStateException.class, properties::validate);
    }

    @Test
    void allowsDisabledIntegrationsToStartWithoutCredentials() {
        AppSecurityProperties properties = new AppSecurityProperties();

        assertDoesNotThrow(properties::validate);
    }

    @Test
    void acceptsCompleteCredentialsForEnabledIntegrations() {
        AppSecurityProperties properties = new AppSecurityProperties();
        properties.getEasyDl().setEnabled(true);
        properties.getEasyDl().setApiKey("api-key");
        properties.getEasyDl().setSecretKey("secret-key");
        properties.getEasyDl().setModelUrl("https://example.invalid/model");
        properties.getPasswordReset().setEnabled(true);
        properties.getPasswordReset().setHmacKey("test-hmac-key-with-at-least-32-bytes");

        assertDoesNotThrow(properties::validate);
    }
}
