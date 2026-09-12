package com.project.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
public class ChangePasswordRequest {

    @JsonProperty("o_password")
    @ToString.Exclude
    private String oldPassword;

    @ToString.Exclude
    private String password;
}
