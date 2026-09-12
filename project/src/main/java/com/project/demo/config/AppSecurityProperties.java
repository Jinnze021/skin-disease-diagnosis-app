package com.project.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
                && (isBlank(easyDl.apiKey) || isBlank(easyDl.secretKey) || isBlank(easyDl.modelUrl))) {
            throw new IllegalStateException(
                    "EasyDL is enabled but EASYDL_API_KEY, EASYDL_SECRET_KEY, or EASYDL_MODEL_URL is missing");
        }
        if (passwordReset.enabled && isBlank(passwordReset.hmacKey)) {
            throw new IllegalStateException(
                    "Password reset is enabled but PASSWORD_RESET_HMAC_KEY is missing");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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
        private String modelUrl = "";
    }

    @Getter
    @Setter
    public static class PasswordReset {
        private boolean enabled = false;
        private String hmacKey = "";
    }
}
