package com.project.demo.dto.auth;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void userResponseContainsNoPasswordOrResetCodeProperty() throws Exception {
        User user = new User();
        user.setUserId(7);
        user.setUsername("demo-user");
        user.setPassword("must-not-appear");
        user.setCode("must-not-appear");

        UserResponse response = UserResponse.from(user);
        String json = objectMapper.writeValueAsString(response);

        assertEquals(7, response.getUserId());
        assertEquals("demo-user", response.getUsername());
        assertFalse(json.contains("password"));
        assertFalse(json.contains("code"));
    }

    @Test
    void userPasswordIsWriteOnlyForJackson() throws Exception {
        User user = objectMapper.readValue("{\"username\":\"demo\",\"password\":\"SafePass123!\"}", User.class);

        assertEquals("SafePass123!", user.getPassword());
        assertFalse(objectMapper.writeValueAsString(user).contains("password"));
        assertFalse(JSON.toJSONString(user).contains("password"));
        assertNull(UserResponse.from(null));
    }
}
