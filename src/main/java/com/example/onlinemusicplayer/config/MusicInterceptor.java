package com.example.onlinemusicplayer.config;

import com.example.onlinemusicplayer.tools.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class MusicInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 我们前面在登录的时候，登录成功的话会创建session，并且在里面设置用户属性
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(Constant.USERINFO_SESSION_KEY) != null) {
            return true; // 表示通过验证，用户处于已登录状态
        }
        // 这里可以加一个重定向，先暂时不管它
        System.out.println("用户未登录");
        return false; // 表示 用户未登录。
    }
}
