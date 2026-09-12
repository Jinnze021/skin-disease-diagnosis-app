package com.project.demo.security;

import javax.servlet.http.HttpServletRequest;

public final class RequestUser {

    public static final String USER_ID_ATTRIBUTE = "authenticatedUserId";
    public static final String USER_GROUP_ATTRIBUTE = "authenticatedUserGroup";

    private RequestUser() {
    }

    public static void attach(HttpServletRequest request, SessionUser user) {
        request.setAttribute(USER_ID_ATTRIBUTE, user.getUserId());
        request.setAttribute(USER_GROUP_ATTRIBUTE, user.getUserGroup());
    }

    public static Integer requireUserId(HttpServletRequest request) {
        Object value = request.getAttribute(USER_ID_ATTRIBUTE);
        if (!(value instanceof Integer)) {
            throw new IllegalStateException("Authenticated user is missing from the request");
        }
        return (Integer) value;
    }

    public static boolean isAdmin(HttpServletRequest request) {
        return "管理员".equals(request.getAttribute(USER_GROUP_ATTRIBUTE));
    }
}
