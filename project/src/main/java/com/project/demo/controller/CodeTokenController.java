package com.project.demo.controller;

import com.project.demo.dto.auth.PasswordResetRequest;
import com.project.demo.security.PasswordResetService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("code_token")
public class CodeTokenController {

    private final PasswordResetService passwordResetService;

    public CodeTokenController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("password-reset/request")
    public Map<String, Object> requestReset(@RequestBody PasswordResetRequest body,
                                             HttpServletRequest request) {
        if (body == null || !StringUtils.hasText(body.getEmail())) {
            return error(30000, "邮箱不能为空");
        }
        try {
            passwordResetService.request(body.getEmail(), request.getRemoteAddr());
            return success("如果该邮箱已注册，验证码将发送至该邮箱");
        } catch (IllegalArgumentException exception) {
            return error(30000, exception.getMessage());
        } catch (IllegalStateException exception) {
            return error(42900, exception.getMessage());
        }
    }

    private Map<String, Object> success(Object result) {
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        return response;
    }

    private Map<String, Object> error(int code, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        return response;
    }
}


