package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import com.bjpowernode.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-04 13:36
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    /**
     * 根据账号密码查询用户
     * @param map
     * @return
     */
    @Override
    public User queryUserByLoginActAndloginPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    /**
     * 查询全部用户信息
     * @return
     */
    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }


}
