package com.example.onlinemusicplayer.service;

import com.example.onlinemusicplayer.mapper.LoveMusicMapper;
import com.example.onlinemusicplayer.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoveMusicService {
    @Autowired
    private LoveMusicMapper loveMusicMapper;

    // 查询此次收藏音乐是否是之前收藏过的
    public Music findLoveMusicByMusicIdAndUserId(Integer userid, Integer musicid) {
        return loveMusicMapper.findLoveMusicByMusicIdAndUserId(userid, musicid);
    }

    // 向数据库 lovemusic插入一条信息
    public boolean insertLoveMusic(Integer userid, Integer musicid) {
        return loveMusicMapper.insertLoveMusic(userid, musicid);
    }
    public List<Music> findLoveMusicByKeyAndUserId(Integer user_id) {
        return loveMusicMapper.findLoveMusicByKeyAndUserId(user_id);
    }
    public List<Music> findLoveMusicByKeyAndUserId(String title, Integer user_id) {
        return loveMusicMapper.findLoveMusicByKeyAndUserId(title, user_id);
    }

    public Integer deleteLoveMusic(Integer userid, Integer musicid) {
        return loveMusicMapper.deleteLoveMusic(userid, musicid);
    }

    public Integer deleteLoveMusicById(Integer musicid) {
        return loveMusicMapper.deleteLoveMusicById(musicid);
    }
}
