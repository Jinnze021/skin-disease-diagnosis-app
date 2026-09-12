package com.project.demo.controller;

import com.project.demo.controller.base.BaseController;
import com.project.demo.dto.auth.ChangePasswordRequest;
import com.project.demo.dto.auth.LoginRequest;
import com.project.demo.dto.auth.RegisterRequest;
import com.project.demo.dto.auth.UserResponse;
import com.project.demo.entity.AccessToken;
import com.project.demo.entity.RegisteredUser;
import com.project.demo.entity.User;
import com.project.demo.entity.UserGroup;
import com.project.demo.security.PasswordService;
import com.project.demo.service.UserGroupService;
import com.project.demo.service.RegisteredUserService;
import com.project.demo.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController extends BaseController<User, UserService> {

    private static final String REGISTERED_USER_GROUP = "注册用户";

    private final UserGroupService userGroupService;
    private final RegisteredUserService registeredUserService;
    private final PasswordService passwordService;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserController(UserService service,
                          UserGroupService userGroupService,
                          RegisteredUserService registeredUserService,
                          PasswordService passwordService,
                          RedisTemplate<String, Object> redisTemplate) {
        setService(service);
        this.userGroupService = userGroupService;
        this.registeredUserService = registeredUserService;
        this.passwordService = passwordService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("register")
    @Transactional
    public Map<String, Object> signUp(@RequestBody RegisterRequest request) {
        if (!StringUtils.hasText(request.getUsername())) {
            return error(30000, "用户名不能为空");
        }
        if (service.usernameExists(request.getUsername())) {
            return error(30000, "用户已存在");
        }

        try {
            User user = fromRegisterRequest(request);
            user.setPassword(passwordService.hash(request.getPassword()));
            service.create(user);
            registeredUserService.create(fromRegisterProfile(request, user.getUserId()));
            return success(UserResponse.from(user));
        } catch (IllegalArgumentException exception) {
            return error(30000, "密码须不少于8个字符、且编码后不超过72字节");
        }
    }

    @PostMapping("forget_password")
    public Map<String, Object> forgetPassword() {
        return error(40300, "密码找回暂未启用，请使用安全验证码流程");
    }

    @PostMapping("login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        User user = findLoginUser(request);
        if (user == null || !passwordService.matches(request.getPassword(), user.getPassword())) {
            return error(30000, "账号或密码不正确");
        }

        Map<String, String> groupQuery = new HashMap<>();
        groupQuery.put("name", user.getUserGroup());
        List groupList = userGroupService.selectBaseList(
                userGroupService.select(groupQuery, new HashMap<>()));
        if (groupList.isEmpty()) {
            return error(30000, "用户组不存在");
        }

        UserGroup userGroup = (UserGroup) groupList.get(0);
        if (StringUtils.hasText(userGroup.getSourceTable())) {
            String examineState = service.selectExamineState(
                    userGroup.getSourceTable(), user.getUserId());
            if (!"已通过".equals(examineState)) {
                return error(30000, "该用户审核未通过");
            }
        }
        if (!Integer.valueOf(1).equals(user.getState())) {
            return error(30000, "用户非可用状态，不能登录");
        }

        if ("face".equals(request.getIsFace())) {
            return userResult(UserResponse.from(user));
        }

        AccessToken accessToken = new AccessToken();
        accessToken.setToken(UUID.randomUUID().toString().replace("-", ""));
        accessToken.setUser_id(user.getUserId());
        redisTemplate.opsForValue().set(accessToken.getToken(), accessToken, Duration.ofHours(2));
        return userResult(UserResponse.from(user, accessToken.getToken()));
    }

    @PostMapping("change_password")
    public Map<String, Object> changePassword(@RequestBody ChangePasswordRequest data,
                                               HttpServletRequest request) {
        Integer userId = tokenGetUserId(request.getHeader("x-auth-token"));
        User user = service.findById(userId);
        if (user == null || !passwordService.matches(data.getOldPassword(), user.getPassword())) {
            return error(10000, "原密码不正确");
        }

        try {
            String encodedPassword = passwordService.hash(data.getPassword());
            if (!service.updatePassword(userId, encodedPassword)) {
                return error(10000, "密码修改失败");
            }
            return success(UserResponse.from(service.findById(userId)));
        } catch (IllegalArgumentException exception) {
            return error(30000, "新密码须不少于8个字符、且编码后不超过72字节");
        }
    }

    @GetMapping("state")
    public Map<String, Object> state(HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        User user = service.findById(tokenGetUserId(token));
        if (user == null) {
            return error(10000, "用户未登录");
        }
        return userResult(UserResponse.from(user, token));
    }

    @GetMapping("quit")
    public Map<String, Object> quit(HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");
        if (StringUtils.hasText(token)) {
            redisTemplate.delete(token);
        }
        return success("退出登录成功");
    }

    public Integer tokenGetUserId(String token) {
        if (!StringUtils.hasText(token)) {
            return 0;
        }
        Object storedToken = redisTemplate.opsForValue().get(token);
        if (!(storedToken instanceof AccessToken)) {
            return 0;
        }
        return ((AccessToken) storedToken).getUser_id();
    }

    @Override
    @RequestMapping("/get_obj")
    public Map<String, Object> obj(HttpServletRequest request) {
        User user = null;
        if (StringUtils.hasText(request.getParameter("user_id"))) {
            try {
                user = service.findById(Integer.valueOf(request.getParameter("user_id")));
            } catch (NumberFormatException ignored) {
                return error(30000, "用户ID格式不正确");
            }
        } else if (StringUtils.hasText(request.getParameter("username"))) {
            user = service.findByUsername(request.getParameter("username"));
        } else if (StringUtils.hasText(request.getParameter("email"))) {
            user = service.findByEmail(request.getParameter("email"));
        } else if (StringUtils.hasText(request.getParameter("phone"))) {
            user = service.findByPhone(request.getParameter("phone"));
        }

        if (user == null) {
            return success(null);
        }
        return userResult(UserResponse.from(user));
    }

    @Override
    @RequestMapping(value = {"/count_group", "/count"})
    public Map<String, Object> count(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (!StringUtils.hasText(username)) {
            return error(30000, "仅支持按用户名统计用户");
        }
        return success(service.usernameExists(username) ? 1 : 0);
    }

    @Override
    @PostMapping("/add")
    @Transactional
    public Map<String, Object> add(HttpServletRequest request) throws IOException {
        Map<String, Object> body = service.readBody(request.getReader());
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(asString(body.get("username")));
        registerRequest.setPassword(asString(body.get("password")));
        registerRequest.setNickname(asString(body.get("nickname")));
        registerRequest.setEmail(asString(body.get("email")));
        registerRequest.setPhone(asString(body.get("phone")));
        registerRequest.setAvatar(asString(body.get("avatar")));
        registerRequest.setOpenId(asString(body.get("open_id")));
        registerRequest.setProfileName(asString(body.get("user_name")));
        registerRequest.setProfileGender(asString(body.get("user_gender")));
        return signUp(registerRequest);
    }

    private User findLoginUser(LoginRequest request) {
        if (StringUtils.hasText(request.getUsername())) {
            return service.findByUsername(request.getUsername());
        }
        if (StringUtils.hasText(request.getEmail())) {
            return service.findByEmail(request.getEmail());
        }
        if (StringUtils.hasText(request.getPhone())) {
            return service.findByPhone(request.getPhone());
        }
        return null;
    }

    private User fromRegisterRequest(RegisterRequest request) {
        User user = new User();
        user.setState(1);
        user.setUserGroup(REGISTERED_USER_GROUP);
        user.setPhone(request.getPhone());
        user.setPhoneState(0);
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setEmailState(0);
        user.setAvatar(request.getAvatar());
        user.setOpen_id(request.getOpenId());
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return user;
    }

    private RegisteredUser fromRegisterProfile(RegisterRequest request, Integer userId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        RegisteredUser profile = new RegisteredUser();
        profile.setUserId(userId);
        profile.setUser_name(StringUtils.hasText(request.getProfileName())
                ? request.getProfileName() : request.getNickname());
        profile.setUser_gender(request.getProfileGender());
        profile.setCreate_time(now);
        profile.setUpdate_time(now);
        return profile;
    }

    private Map<String, Object> userResult(UserResponse user) {
        Map<String, Object> result = new HashMap<>();
        result.put("obj", user);
        return success(result);
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
