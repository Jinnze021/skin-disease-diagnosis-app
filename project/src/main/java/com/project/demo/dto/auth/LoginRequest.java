package com.project.demo.dto.auth;

import lombok.Data;
import lombok.ToString;

@Data
public class LoginRequest {
    private String username;
    private String email;
    private String phone;
    @ToString.Exclude
    private String password;
    private String isFace;
}
