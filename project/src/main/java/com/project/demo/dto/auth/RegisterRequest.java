package com.project.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
public class RegisterRequest {
    private String username;
    @ToString.Exclude
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;

    @JsonProperty("open_id")
    private String openId;

    @JsonProperty("user_name")
    private String profileName;

    @JsonProperty("user_gender")
    private String profileGender;
}
