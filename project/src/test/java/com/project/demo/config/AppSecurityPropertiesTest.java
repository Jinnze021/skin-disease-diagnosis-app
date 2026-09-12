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
        properties.getEasyDl().setModelUrl(
                "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/FAKE_MODEL");
        properties.getPasswordReset().setEnabled(true);
        properties.getPasswordReset().setHmacKey("test-hmac-key-with-at-least-32-bytes");

        assertDoesNotThrow(properties::validate);
    }

    @Test
    void rejectsNonBaiduEasyDlEndpoints() {
        AppSecurityProperties properties = new AppSecurityProperties();
        properties.getEasyDl().setEnabled(true);
        properties.getEasyDl().setApiKey("FAKE_API_KEY");
        properties.getEasyDl().setSecretKey("FAKE_SECRET_KEY");
        properties.getEasyDl().setTokenUrl("https://attacker.invalid/oauth/2.0/token");
        properties.getEasyDl().setModelUrl(
                "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/classification/FAKE_MODEL");

        assertThrows(IllegalStateException.class, properties::validate);
    }
}
