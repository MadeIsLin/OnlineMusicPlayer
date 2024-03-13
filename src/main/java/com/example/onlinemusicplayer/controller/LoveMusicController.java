package com.example.onlinemusicplayer.controller;


import com.example.onlinemusicplayer.service.LoveMusicService;
import com.example.onlinemusicplayer.model.Music;
import com.example.onlinemusicplayer.model.User;
import com.example.onlinemusicplayer.tools.Constant;
import com.example.onlinemusicplayer.tools.ResponseBodyMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/lovemusic")
public class LoveMusicController {
    @Autowired
    LoveMusicService loveMusicService;

    @Transactional
    @RequestMapping(value = "/likemusic")
    public ResponseBodyMessage<Boolean> likeMusic(@RequestParam Integer musicid, HttpServletRequest request) {
        // 获取用户id
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        Integer userid = user.getId();

        // 验证 上传歌曲是否已经被上传
        Music music = loveMusicService.findLoveMusicByMusicIdAndUserId(userid, musicid);
        if (music != null) {
            return new ResponseBodyMessage<>(-1, "歌曲已收藏，请重新选择", false);
        }
        if (loveMusicService.insertLoveMusic(userid, musicid)) {
            return new ResponseBodyMessage<>(0, "添加成功", true);
        }
        return new ResponseBodyMessage<>(-1, "添加失败，请稍后再试", false);
    }

    @RequestMapping(value = "/findlovemusic")
    public ResponseBodyMessage<List<Music>> findLoveMusic(@RequestParam(required = false) String title, HttpServletRequest request) {
        // 获取用户id
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        Integer userid = user.getId();

        // 验证歌曲是否收藏
        List<Music> list = null;
        if (title == null) {
            list = loveMusicService.findLoveMusicByKeyAndUserId(userid);
        } else {
            list = loveMusicService.findLoveMusicByKeyAndUserId(title, userid);
        }
        return new ResponseBodyMessage<>(0, "查询到了相关收藏的音乐", list);
    }

    @RequestMapping(value = "/deletelovemusic")
    @Transactional
    public ResponseBodyMessage<Boolean> deleteLoveMusic(@RequestParam Integer musicid, HttpServletRequest request) {
        // 获取 用户id
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        Integer userid = user.getId();

        // 移除收藏音乐
        Integer ret = loveMusicService.deleteLoveMusic(userid, musicid);
        if (ret != 1) {
            return new ResponseBodyMessage<>(-1, "取消收藏失败!", false);
        }
        return new ResponseBodyMessage<>(0, "取消收藏成功", true);
    }
}
