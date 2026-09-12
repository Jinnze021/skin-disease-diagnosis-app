package com.project.demo.security;

import java.io.Serializable;

public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer userId;
    private final String userGroup;

    public SessionUser(Integer userId, String userGroup) {
        this.userId = userId;
        this.userGroup = userGroup;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public boolean isAdmin() {
        return "管理员".equals(userGroup);
    }
}
