package com.project.demo.dao;

import com.project.demo.dao.base.BaseMapper;
import com.project.demo.entity.RegisteredUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 注册用户：(RegisteredUser)Mapper接口
 *
 */
@Mapper
public interface RegisteredUserMapper extends BaseMapper<RegisteredUser>{

}
