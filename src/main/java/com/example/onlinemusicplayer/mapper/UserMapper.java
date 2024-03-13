package com.example.onlinemusicplayer.mapper;

import com.example.onlinemusicplayer.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 登录方法
    User login(User loginUser);
    // 根据用户名称查询用户信息
    User selectByName(@Param("username") String username);
}
