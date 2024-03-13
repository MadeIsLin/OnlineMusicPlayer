package com.example.onlinemusicplayer.tools;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    // 定义一个固定的盐值
    private static final String salt = "1b2i3t4e";

    // 定义一个 调用md5加密的方法
    public static String md5(String src) {
        return DigestUtils.md5Hex(src); // 进行 md5 加密
    }

    // 第一次加密（前端加密），模拟前端自己加密，然后传到后端
    public static String inputPassToFormPass(String inputPass) {
        // 先对密码加盐
        String str = "" + salt.charAt(1) + salt.charAt(3) + inputPass + salt.charAt(5) + salt.charAt(6);
        // 然后，进行一次 md5 加密
        return md5(str);
    }

    // 第二次 md5 加密（服务器加密）
    // 前端加密过的密码，传给后端，由后端进行第二次加密
    public static String formPassToDBPass(String formPass, String salt) {
        // 先对密码加盐
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        // 然后，再进行一次 md5 加密
        return md5(str);
    }

    // 上面两个函数合到一起进行调用
    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass); // 第一次加密
        String dbPass = formPassToDBPass(formPass, saltDB); // 第二次加密
        return dbPass; // 最终生成的密码，才是存入数据库中的密码
    }

    // 效果演示
    public static void main(String[] args) {
        System.out.println("对用户输入密码进行第一次加密：" + inputPassToFormPass("123456"));
        System.out.println("对用户输入密码进行第二次加密：" + formPassToDBPass(inputPassToFormPass("123456"), "1b2i3t4e"));
        System.out.println("对用户输入密码进行第2次加密：" + inputPassToDbPass("123456", "1b2i3t4e"));

    }
}
