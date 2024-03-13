package com.example.onlinemusicplayer.tools;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        // 模拟从前端获取的密码
        String password = "123456";
        // 新建一个BCryptPasswordEncoder
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // 对密码进行加密
        String newPassword = bCryptPasswordEncoder.encode(password);
        // 输出加密之后的结果
        System.out.println("加密的密码为：" + newPassword);

        // 使用matches方法进行密码的校验
        boolean same_password_result = bCryptPasswordEncoder.matches(password, newPassword);

        // 返回true
        System.out.println("加密的密码和正确密码对比结果：" + same_password_result);

        boolean other_password_result = bCryptPasswordEncoder.matches("987654", newPassword);

        // 返回false
        System.out.println("加密的密码和错误的密码对比结果：" + other_password_result);

    }
}
