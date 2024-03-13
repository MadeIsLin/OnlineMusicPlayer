package com.example.onlinemusicplayer.model;

import lombok.Data;

@Data
public class Music {
    private Integer id;
    private String title;
    private String singer;
    private String time;
    private String url;
    private Integer userid;
}
