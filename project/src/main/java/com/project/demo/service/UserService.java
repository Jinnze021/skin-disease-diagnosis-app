package com.project.demo.service;

import com.project.demo.dao.UserMapper;
import com.project.demo.entity.User;
import com.project.demo.service.base.BaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户账户：用于保存用户登录信息(User)表服务接口
 *
 */
@Service
public class UserService extends BaseService<User> {

    @Autowired
    UserMapper userMapper;

    public String selectExamineState(String sourceTable, Integer userId) {
        if (!"registered_user".equals(sourceTable)) {
            throw new IllegalArgumentException("Unsupported user source table");
        }
        return userMapper.selectRegisteredUserExamineState(userId);
    }

    public User findById(Integer userId) {
        return userId == null ? null : userMapper.selectById(userId);
    }

    public User findByUsername(String username) {
        return findFirst(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    public User findByEmail(String email) {
        return findFirst(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
    }

    public User findByPhone(String phone) {
        return findFirst(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
    }

    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    public int create(User user) {
        return userMapper.insert(user);
    }

    public boolean updatePassword(Integer userId, String encodedPassword) {
        if (userId == null || encodedPassword == null) {
            return false;
        }
        return userMapper.update(null, new LambdaUpdateWrapper<User>()
                .eq(User::getUserId, userId)
                .set(User::getPassword, encodedPassword)) == 1;
    }

    private User findFirst(LambdaQueryWrapper<User> wrapper) {
        wrapper.last("LIMIT 1");
        return userMapper.selectOne(wrapper);
    }
}


