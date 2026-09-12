package com.project.demo.security;

public class InvalidResetCodeException extends RuntimeException {

    public InvalidResetCodeException() {
        super("验证码无效或已过期");
    }
}
