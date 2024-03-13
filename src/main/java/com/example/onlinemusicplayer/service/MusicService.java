package com.example.onlinemusicplayer.service;

import com.example.onlinemusicplayer.mapper.MusicMapper;
import com.example.onlinemusicplayer.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {
    @Autowired
    private MusicMapper musicMapper;

    public Integer insert(String title, String singer, String time,
                          String url, Integer userid) {
        return musicMapper.insert(title, singer, time, url, userid);
    }

    // 查询上传歌曲是否已经在数据库存在
    public Integer select(String title, String singer) {
        return musicMapper.select(title, singer);
    }

    // 查询当前要删除的音乐是否在数据中存在。
    public Music selectMusicById(Integer id) {
        return musicMapper.selectMusicById(id);
    }

    // 删除指定id的音乐
    public Integer deleteMusicById(Integer id) {
        return musicMapper.deleteMusicById(id);
    }

    // 模糊查询
    public List<Music> findByMusicByName(String name) {
        return musicMapper.findByMusicByName(name);
    }

    // 查询全部
    public List<Music> findByMusicByName() {
        return musicMapper.findByMusicByName();
    }
}
