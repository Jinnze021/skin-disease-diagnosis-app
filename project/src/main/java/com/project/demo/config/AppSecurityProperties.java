package com.project.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Component
@ConfigurationProperties(prefix = "app")
public class AppSecurityProperties {

    private final Cors cors = new Cors();
    private final Upload upload = new Upload();
    private final EasyDl easyDl = new EasyDl();
    private final PasswordReset passwordReset = new PasswordReset();

    @PostConstruct
    public void validate() {
        if (easyDl.enabled
                && (isBlank(easyDl.apiKey) || isBlank(easyDl.secretKey)
                || isBlank(easyDl.tokenUrl) || isBlank(easyDl.modelUrl))) {
            throw new IllegalStateException(
                    "EasyDL is enabled but its required environment variables are incomplete");
        }
        if (easyDl.enabled) {
            requireTrustedEasyDlUrl(easyDl.tokenUrl, "/oauth/2.0/token");
            requireTrustedEasyDlUrl(easyDl.modelUrl, "/rpc/2.0/ai_custom/");
        }
        if (passwordReset.enabled && (isBlank(passwordReset.hmacKey)
                || passwordReset.hmacKey.getBytes(StandardCharsets.UTF_8).length < 32)) {
            throw new IllegalStateException(
                    "Password reset is enabled but PASSWORD_RESET_HMAC_KEY is missing or shorter than 32 bytes");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static void requireTrustedEasyDlUrl(String value, String requiredPathPrefix) {
        try {
            URI uri = new URI(value);
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || !"aip.baidubce.com".equalsIgnoreCase(uri.getHost())
                    || uri.getRawUserInfo() != null
                    || uri.getPort() != -1
                    || uri.getPath() == null
                    || !uri.getPath().startsWith(requiredPathPrefix)) {
                throw new IllegalStateException("EasyDL endpoint must use the trusted Baidu HTTPS host");
            }
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("EasyDL endpoint is invalid", exception);
        }
    }

    @Getter
    @Setter
    public static class Cors {
        private List<String> allowedOrigins = new ArrayList<>(
                Collections.singletonList("http://localhost:8080"));
    }

    @Getter
    @Setter
    public static class Upload {
        private String dir = "./uploads";
        private long maxBytes = 5_242_880L;
    }

    @Getter
    @Setter
    public static class EasyDl {
        private boolean enabled = false;
        private String apiKey = "";
        private String secretKey = "";
        private String tokenUrl = "https://aip.baidubce.com/oauth/2.0/token";
        private String modelUrl = "";
    }

    @Getter
    @Setter
    public static class PasswordReset {
        private boolean enabled = false;
        private String hmacKey = "";
    }
}
