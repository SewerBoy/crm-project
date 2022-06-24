package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ccstart
 * @Description
 * @create 2022-05-04 13:33
 */
@Service
public interface UserService {
    /**
     * 根据账号密码查询用户信息
     * @param map
     * @return
     */
    User queryUserByLoginActAndloginPwd(Map<String, Object> map);

    /**
     * 查询全部用户信息
     * @return
     */
    List<User> selectAllUser();


}
