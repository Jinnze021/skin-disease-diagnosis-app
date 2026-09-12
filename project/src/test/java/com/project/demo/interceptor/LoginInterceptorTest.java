package com.project.demo.interceptor;

import com.project.demo.security.AccessLevel;
import com.project.demo.security.EndpointAccessPolicy;
import com.project.demo.security.RequestUser;
import com.project.demo.security.SessionUser;
import com.project.demo.security.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginInterceptorTest {

    private TokenService tokenService;
    private EndpointAccessPolicy policy;
    private LoginInterceptor interceptor;

    @BeforeEach
    void setUp() {
        tokenService = mock(TokenService.class);
        policy = new EndpointAccessPolicy();
        interceptor = new LoginInterceptor(policy, tokenService);
    }

    @Test
    void exposesOnlyExplicitPublicEndpoints() throws Exception {
        assertEquals(AccessLevel.PUBLIC, policy.requiredAccess("POST", "/user/login"));
        assertEquals(AccessLevel.PUBLIC, policy.requiredAccess("POST", "/user/register"));
        assertEquals(AccessLevel.PUBLIC,
                policy.requiredAccess("POST", "/code_token/password-reset/request"));
        assertEquals(AccessLevel.PUBLIC,
                policy.requiredAccess("POST", "/user/password-reset/confirm"));
        assertEquals(AccessLevel.PUBLIC, policy.requiredAccess("GET", "/article/get_list"));
        assertEquals(AccessLevel.PUBLIC, policy.requiredAccess("OPTIONS", "/anything"));
        assertEquals(AccessLevel.AUTHENTICATED, policy.requiredAccess("POST", "/ai/skin-diagnosis"));
        assertEquals(AccessLevel.ADMIN, policy.requiredAccess("POST", "/article/add"));
        assertEquals(AccessLevel.ADMIN, policy.requiredAccess("GET", "/article/del"));
        assertEquals(AccessLevel.AUTHENTICATED, policy.requiredAccess("POST", "/upload/image"));
        assertEquals(AccessLevel.AUTHENTICATED,
                policy.requiredAccess("POST", "/hospital_information/upload"));
        assertEquals(AccessLevel.PUBLIC,
                policy.requiredAccess("GET", "/upload/files/123e4567-e89b-12d3-a456-426614174000.png"));
        assertEquals(AccessLevel.ADMIN, policy.requiredAccess("GET", "/user/get_list"));

        MockHttpServletRequest request = request("POST", "/api/user/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(200, response.getStatus());
    }

    @Test
    void rejectsMissingOrInvalidTokenWithJson401() throws Exception {
        MockHttpServletRequest request = request("POST", "/api/ai/skin-diagnosis");
        request.addHeader("Origin", "https://attacker.example");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(401, response.getStatus());
        assertEquals("application/json;charset=UTF-8", response.getContentType());
        assertTrue(response.getContentAsString().contains("40100"));
        assertNull(response.getHeader("Access-Control-Allow-Origin"));
    }

    @Test
    void attachesAuthenticatedIdentityForProtectedEndpoint() throws Exception {
        SessionUser sessionUser = new SessionUser(21, "注册用户");
        when(tokenService.resolve("opaque-token")).thenReturn(Optional.of(sessionUser));
        MockHttpServletRequest request = request("POST", "/api/ai/skin-diagnosis");
        request.addHeader("x-auth-token", "opaque-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
        assertEquals(21, request.getAttribute(RequestUser.USER_ID_ATTRIBUTE));
        assertEquals("注册用户", request.getAttribute(RequestUser.USER_GROUP_ATTRIBUTE));
        assertEquals(200, response.getStatus());
    }

    @Test
    void forbidsNonAdminFromAdministrativeEndpoint() throws Exception {
        when(tokenService.resolve("member-token"))
                .thenReturn(Optional.of(new SessionUser(22, "注册用户")));
        MockHttpServletRequest request = request("DELETE", "/api/user/del");
        request.addHeader("x-auth-token", "member-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertFalse(interceptor.preHandle(request, response, new Object()));
        assertEquals(403, response.getStatus());
        assertTrue(response.getContentAsString().contains("40300"));
    }

    @Test
    void allowsAdminOnAdministrativeEndpoint() throws Exception {
        when(tokenService.resolve("admin-token"))
                .thenReturn(Optional.of(new SessionUser(1, "管理员")));
        MockHttpServletRequest request = request("POST", "/api/auth/add");
        request.addHeader("x-auth-token", "admin-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertTrue(interceptor.preHandle(request, response, new Object()));
    }

    private static MockHttpServletRequest request(String method, String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, uri);
        request.setContextPath("/api");
        return request;
    }
}
