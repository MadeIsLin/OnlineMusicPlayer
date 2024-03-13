package com.example.onlinemusicplayer.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTime {
    public static void main(String[] args) {
        // 通过SimpleDateFormat 来规定时间的格式，不了解可以去百度
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        // 通过Date类来获取当前系统时间，再按照SimpleDateFormat规定日期形式进行转换
        String time = sf.format(new Date());
        System.out.println("当前时间：" + time);

        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time2 = sf2.format(new Date());
        System.out.println("当前时间：" + time2);

    }
}
