package com.project.demo.security;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Component
public class EndpointAccessPolicy {

    private static final Set<String> PUBLIC_POST_ENDPOINTS = new HashSet<>(Arrays.asList(
            "/user/login",
            "/user/register",
            "/code_token/password-reset/request",
            "/user/password-reset/confirm"
    ));

    private static final Set<String> PUBLIC_CONTENT_RESOURCES = new HashSet<>(Arrays.asList(
            "/article",
            "/article_type",
            "/knowledge_popularization",
            "/notice",
            "/slides",
            "/hospital_information",
            "/forum",
            "/forum_type"
    ));

    private static final Set<String> READ_ACTIONS = new HashSet<>(Arrays.asList(
            "get_obj", "get_list", "list_group", "bar_group",
            "count_group", "count", "sum_group", "sum", "avg_group", "avg",
            "get_hits_list"
    ));

    public AccessLevel requiredAccess(String method, String requestPath) {
        String normalizedMethod = method == null ? "" : method.toUpperCase(Locale.ROOT);
        String path = normalizePath(requestPath);

        if ("OPTIONS".equals(normalizedMethod)) {
            return AccessLevel.PUBLIC;
        }
        if ("POST".equals(normalizedMethod) && PUBLIC_POST_ENDPOINTS.contains(path)) {
            return AccessLevel.PUBLIC;
        }
        if ("GET".equals(normalizedMethod) && isPublicStaticResource(path)) {
            return AccessLevel.PUBLIC;
        }
        if ("GET".equals(normalizedMethod) && isPublicContentRead(path)) {
            return AccessLevel.PUBLIC;
        }

        if (isResource(path, "/user_group") || isResource(path, "/auth")) {
            return AccessLevel.ADMIN;
        }
        if (isResource(path, "/user")) {
            if (path.equals("/user/state") || path.equals("/user/quit")
                    || path.equals("/user/change_password") || path.equals("/user/get_obj")) {
                return AccessLevel.AUTHENTICATED;
            }
            return AccessLevel.ADMIN;
        }
        if (isContentResource(path) && !"GET".equals(normalizedMethod)) {
            return AccessLevel.ADMIN;
        }
        if (isContentResource(path)) {
            return AccessLevel.ADMIN;
        }

        return AccessLevel.AUTHENTICATED;
    }

    private boolean isPublicContentRead(String path) {
        for (String resource : PUBLIC_CONTENT_RESOURCES) {
            if (isResource(path, resource)) {
                String action = path.substring(resource.length());
                if (action.startsWith("/")) {
                    action = action.substring(1);
                }
                return READ_ACTIONS.contains(action);
            }
        }
        return false;
    }

    private boolean isContentResource(String path) {
        for (String resource : PUBLIC_CONTENT_RESOURCES) {
            if (isResource(path, resource)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPublicStaticResource(String path) {
        return path.equals("/")
                || path.equals("/favicon.ico")
                || isResource(path, "/admin")
                || isResource(path, "/static")
                || path.matches("^/upload/[^/]+\\.[A-Za-z0-9]{1,10}$");
    }

    private boolean isResource(String path, String resource) {
        return path.equals(resource) || path.startsWith(resource + "/");
    }

    private String normalizePath(String requestPath) {
        if (requestPath == null || requestPath.trim().isEmpty()) {
            return "/";
        }
        String path = requestPath.trim();
        int queryIndex = path.indexOf('?');
        if (queryIndex >= 0) {
            path = path.substring(0, queryIndex);
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        while (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}
