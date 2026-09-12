package com.project.demo.config;

import com.project.demo.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final AppSecurityProperties securityProperties;

    public WebAppConfig(LoginInterceptor loginInterceptor, AppSecurityProperties securityProperties) {
        this.loginInterceptor = loginInterceptor;
        this.securityProperties = securityProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = securityProperties.getCors().getAllowedOrigins().toArray(new String[0]);
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "x-auth-token")
                .exposedHeaders("x-auth-token")
                .allowCredentials(true)
                .maxAge(1800L);
    }
}
