package com.project.demo.interceptor;

import com.project.demo.security.AccessLevel;
import com.project.demo.security.EndpointAccessPolicy;
import com.project.demo.security.RequestUser;
import com.project.demo.security.SessionUser;
import com.project.demo.security.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final EndpointAccessPolicy accessPolicy;
    private final TokenService tokenService;

    public LoginInterceptor(EndpointAccessPolicy accessPolicy, TokenService tokenService) {
        this.accessPolicy = accessPolicy;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AccessLevel required = accessPolicy.requiredAccess(request.getMethod(), applicationPath(request));
        if (required == AccessLevel.PUBLIC) {
            return true;
        }

        Optional<SessionUser> sessionUser = tokenService.resolve(request.getHeader(TokenService.TOKEN_HEADER));
        if (!sessionUser.isPresent()) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, 40100, "未登录或登录已过期");
            return false;
        }

        if (required == AccessLevel.ADMIN && !sessionUser.get().isAdmin()) {
            writeError(response, HttpServletResponse.SC_FORBIDDEN, 40300, "无权访问该接口");
            return false;
        }

        RequestUser.attach(request, sessionUser.get());
        return true;
    }

    private String applicationPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && uri.startsWith(contextPath)) {
            uri = uri.substring(contextPath.length());
        }
        return uri.isEmpty() ? "/" : uri;
    }

    private void writeError(HttpServletResponse response, int httpStatus, int code, String message)
            throws IOException {
        response.setStatus(httpStatus);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":{\"code\":" + code
                + ",\"message\":\"" + message + "\"}}");
    }
}
