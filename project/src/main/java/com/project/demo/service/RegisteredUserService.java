package com.project.demo.service;

import com.project.demo.dao.RegisteredUserMapper;
import com.project.demo.entity.RegisteredUser;
import com.project.demo.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 注册用户：(RegisteredUser)表服务接口
 *
 */
@Service
public class RegisteredUserService extends BaseService<RegisteredUser> {

    @Autowired
    private RegisteredUserMapper registeredUserMapper;

    public int create(RegisteredUser registeredUser) {
        return registeredUserMapper.insert(registeredUser);
    }
}
