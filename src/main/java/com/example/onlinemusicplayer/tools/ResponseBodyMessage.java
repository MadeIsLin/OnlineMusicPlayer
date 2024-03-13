package com.example.onlinemusicplayer.tools;

import lombok.Data;

@Data
public class ResponseBodyMessage <T>{
    private int status; // 状态码
    private String message; // 返回的信息是【出错的原因】
    private T date; // 返回给前端的数据

    public ResponseBodyMessage(int status, String message, T date) {
        this.status = status;
        this.message = message;
        this.date = date;
    }
}
