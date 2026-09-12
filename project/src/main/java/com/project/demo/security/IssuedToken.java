package com.project.demo.security;

public class IssuedToken {

    private final String token;
    private final long expiresInSeconds;

    public IssuedToken(String token, long expiresInSeconds) {
        this.token = token;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getToken() {
        return token;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }
}
