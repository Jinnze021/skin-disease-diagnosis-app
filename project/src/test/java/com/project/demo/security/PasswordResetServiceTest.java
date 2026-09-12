package com.project.demo.security;

import com.project.demo.config.AppSecurityProperties;
import com.project.demo.controller.CodeTokenController;
import com.project.demo.entity.User;
import com.project.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PasswordResetServiceTest {

    private final Map<String, Object> redisValues = new ConcurrentHashMap<>();
    private final Map<String, Long> counters = new ConcurrentHashMap<>();

    private RedisTemplate<String, Object> redisTemplate;
    private ValueOperations<String, Object> valueOperations;
    private JavaMailSender mailSender;
    private UserService userService;
    private PasswordService passwordService;
    private VerificationCodeGenerator codeGenerator;
    private AppSecurityProperties properties;
    private Clock clock;
    private PasswordResetService service;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        mailSender = mock(JavaMailSender.class);
        userService = mock(UserService.class);
        passwordService = mock(PasswordService.class);
        codeGenerator = mock(VerificationCodeGenerator.class);
        properties = new AppSecurityProperties();
        properties.getPasswordReset().setEnabled(true);
        properties.getPasswordReset().setHmacKey("test-only-hmac-key-with-at-least-32-bytes");
        clock = Clock.fixed(Instant.parse("2026-09-12T06:00:00Z"), ZoneOffset.UTC);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doAnswer(invocation -> {
            redisValues.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(valueOperations).set(anyString(), any(), any(Duration.class));
        when(valueOperations.setIfAbsent(anyString(), any(), any(Duration.class)))
                .thenAnswer(invocation -> redisValues.putIfAbsent(
                        invocation.getArgument(0), invocation.getArgument(1)) == null);
        when(valueOperations.getAndDelete(anyString()))
                .thenAnswer(invocation -> redisValues.remove(invocation.getArgument(0)));
        when(valueOperations.increment(anyString()))
                .thenAnswer(invocation -> counters.merge(invocation.getArgument(0), 1L, Long::sum));
        when(redisTemplate.expire(anyString(), any(Duration.class))).thenReturn(true);
        when(redisTemplate.delete(anyString()))
                .thenAnswer(invocation -> redisValues.remove(invocation.getArgument(0)) != null);

        when(codeGenerator.generate()).thenReturn("123456");
        when(passwordService.hash("SafePass123!")).thenReturn("encoded-password");

        User user = new User();
        user.setUserId(7);
        user.setEmail("demo@example.invalid");
        when(userService.findByEmail("demo@example.invalid")).thenReturn(user);
        when(userService.updatePassword(7, "encoded-password")).thenReturn(true);

        service = new PasswordResetService(redisTemplate, mailSender, userService,
                passwordService, codeGenerator, properties, clock);
    }

    @Test
    void validCodeCanBeUsedOnlyOnce() {
        service.request(" Demo@Example.Invalid ", "127.0.0.1");

        service.confirm("demo@example.invalid", "123456", "SafePass123!");

        assertThrows(InvalidResetCodeException.class,
                () -> service.confirm("demo@example.invalid", "123456", "SafePass123!"));
        verify(userService).updatePassword(7, "encoded-password");
        assertFalse(redisValues.keySet().stream().anyMatch(key -> key.contains("demo@example.invalid")));
        assertFalse(redisValues.values().stream().anyMatch(value -> String.valueOf(value).contains("123456")));
    }

    @Test
    void fiveWrongAttemptsInvalidateTheCode() {
        service.request("demo@example.invalid", "127.0.0.1");

        for (int attempt = 0; attempt < 5; attempt++) {
            assertThrows(InvalidResetCodeException.class,
                    () -> service.confirm("demo@example.invalid", "000000", "SafePass123!"));
        }

        assertThrows(InvalidResetCodeException.class,
                () -> service.confirm("demo@example.invalid", "123456", "SafePass123!"));
        verify(userService, never()).updatePassword(any(), anyString());
    }

    @Test
    void expiredCodeCannotResetPassword() {
        service.request("demo@example.invalid", "127.0.0.1");
        PasswordResetService laterService = new PasswordResetService(redisTemplate, mailSender, userService,
                passwordService, codeGenerator, properties,
                Clock.fixed(Instant.parse("2026-09-12T06:11:00Z"), ZoneOffset.UTC));

        assertThrows(InvalidResetCodeException.class,
                () -> laterService.confirm("demo@example.invalid", "123456", "SafePass123!"));
        verify(userService, never()).updatePassword(any(), anyString());
    }

    @Test
    void requestEndpointNeverReturnsTheCode() throws Exception {
        PasswordResetService resetService = mock(PasswordResetService.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new CodeTokenController(resetService)).build();

        mockMvc.perform(post("/code_token/password-reset/request")
                        .with(request -> {
                            request.setRemoteAddr("127.0.0.1");
                            return request;
                        })
                        .contentType(APPLICATION_JSON)
                        .content("{\"email\":\"demo@example.invalid\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("123456"))));
    }
}
