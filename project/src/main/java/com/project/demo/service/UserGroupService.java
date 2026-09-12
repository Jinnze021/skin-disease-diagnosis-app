package com.project.demo.service;

import com.project.demo.entity.UserGroup;
import com.project.demo.service.base.BaseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

/**
 * 用户组：用于用户前端身份和鉴权(UserGroup)表服务接口
 *
 */
@Service
public class UserGroupService extends BaseService<UserGroup> {

    public UserGroup findByName(String name) {
        LambdaQueryWrapper<UserGroup> query = new LambdaQueryWrapper<UserGroup>()
                .eq(UserGroup::getName, name)
                .last("LIMIT 1");
        return baseMapper.selectOne(query);
    }
}


