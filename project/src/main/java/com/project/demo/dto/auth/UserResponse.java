package com.project.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.demo.entity.User;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UserResponse {
    private Integer userId;
    private Integer state;
    private String userGroup;
    private Timestamp loginTime;
    private String phone;
    private Integer phoneState;
    private String username;
    private String nickname;
    private String email;
    private Integer emailState;
    private String avatar;
    private String openId;
    private Timestamp createTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    private UserResponse() {
    }

    public static UserResponse from(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.userId = user.getUserId();
        response.state = user.getState();
        response.userGroup = user.getUserGroup();
        response.loginTime = user.getLoginTime();
        response.phone = user.getPhone();
        response.phoneState = user.getPhoneState();
        response.username = user.getUsername();
        response.nickname = user.getNickname();
        response.email = user.getEmail();
        response.emailState = user.getEmailState();
        response.avatar = user.getAvatar();
        response.openId = user.getOpen_id();
        response.createTime = user.getCreateTime();
        return response;
    }

    public static UserResponse from(User user, String token) {
        UserResponse response = from(user);
        if (response != null) {
            response.token = token;
        }
        return response;
    }
}
