package com.example.onlinemusicplayer.controller;

import com.example.onlinemusicplayer.model.User;
import com.example.onlinemusicplayer.service.UserService;
import com.example.onlinemusicplayer.tools.Constant;
import com.example.onlinemusicplayer.tools.ResponseBodyMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 这个controller, 就负责关于 用户登录
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @RequestMapping(value = "/login")
    public ResponseBodyMessage<User> login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // 根据 前端传递的 用户名 username，来查询对应的用户信息
        User user = userService.selectByName(username);

        // 对查询到的结果，进行判断
        if (user == null) {
            // 登录失败【用户名不存在】
            // 状态码为负数表示，登录失败。message是错误信息，userLogin是用户登录失败了
            return new ResponseBodyMessage<>(-1, "登录失败", user);
        } else {
            // 用户名存在
            // 判断密码是否和前端传递的数据是否一致的。
            boolean flag = bCryptPasswordEncoder.matches(password, user.getPassword());
            if (flag) {
                HttpSession session = request.getSession(true);
                user.setPassword("");
                session.setAttribute(Constant.USERINFO_SESSION_KEY, user);
                return new ResponseBodyMessage<>(0, "登录成功", user);
            }
            user.setPassword("");
            return new ResponseBodyMessage<>(-1, "登录失败", user);
        }
    }



    @Autowired
    UserService userService;

    @RequestMapping(value = "/login2")
    public ResponseBodyMessage<User> login2(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // 由于我们在mapper中定义的方法的参数是一个User对象，所以，需要将其包装一下
        User userLogin = new User();
        userLogin.setUsername(username);
        userLogin.setPassword(password);
        User user = userService.login(userLogin);
        // 对查询到的结果，进行判断
        if (user == null) {
            System.out.println("登陆失败");
            // 登录失败【用户名不存在】
            // 状态码为负数表示登陆失败。message是错误信息，userLogin 是 用户登录失败了。
            return new ResponseBodyMessage<>(-1, "登录失败", userLogin);
        } else {
            // 登录成功
            HttpSession session = request.getSession(true);
            user.setPassword("");
            session.setAttribute(Constant.USERINFO_SESSION_KEY, userLogin);
            userLogin.setPassword("");
            return new ResponseBodyMessage<>(0, "登录成功", userLogin);
        }
    }


    @RequestMapping(value = "/login1")
    public ResponseBodyMessage<User> login1(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {

        // 根据 前端传递的用户名username，来查询对应的用户信息
        User user = userService.selectByName(username);

        // 对查询的结果，进行判断
        if (user == null) {
            // 登录失败【用户名不存在】
            // 状态码为负数表示登录失败，message 是错误信息，userLogin是用户登录失败了

            return new ResponseBodyMessage<>(-1, "登录失败", user);
        }else {
            // 用户名存在
            // 判断 密码 是否和前端传递的数据是一致的。
            boolean flag = bCryptPasswordEncoder.matches(password, user.getPassword());//bCryptPasswordEncoder.matches(password, user.getPassword());
            if (flag) {
                HttpSession session = request.getSession(true);
                user.setPassword("");
                session.setAttribute(Constant.USERINFO_SESSION_KEY, user);
                return new ResponseBodyMessage<>(0, "登录失败", user);
            }
            user.setPassword("");
            return new ResponseBodyMessage<>(-1, "登陆失败", user);
        }

    }
}
