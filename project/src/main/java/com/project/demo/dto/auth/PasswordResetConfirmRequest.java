package com.project.demo.dto.auth;

import lombok.Data;
import lombok.ToString;

@Data
public class PasswordResetConfirmRequest {
    private String email;
    @ToString.Exclude
    private String code;
    @ToString.Exclude
    private String newPassword;
}
