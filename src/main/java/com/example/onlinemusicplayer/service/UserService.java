package com.example.onlinemusicplayer.service;

import com.example.onlinemusicplayer.mapper.UserMapper;
import com.example.onlinemusicplayer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    // 根据用户名和密码登录的方式
    public User login(User userLogin) {
        return userMapper.login(userLogin);
    }

    // 根据用户名查询对应的用户信息
    public User selectByName(String username) {
        return userMapper.selectByName(username);
    }
}
