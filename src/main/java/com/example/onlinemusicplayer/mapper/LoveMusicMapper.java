package com.example.onlinemusicplayer.mapper;

import com.example.onlinemusicplayer.model.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface LoveMusicMapper {
    // 需要查询此次收藏音乐是否是之前收藏过的
    Music findLoveMusicByMusicIdAndUserId(@Param("userid") Integer userid, @Param("musicid") Integer musicid);
    
    // 向数据表lovemusic插入一条信息
    boolean insertLoveMusic(@Param("userid") Integer userid, @Param("musicid") Integer musicid);

    // 如果没有传入 歌曲名/字符，显示当前用户收藏的所有音乐
    List<Music> findLoveMusicByKeyAndUserId(@Param("user_id") Integer user_id);

    // 反之，传入了 歌曲名/字符，进行模糊查询。
    List<Music> findLoveMusicByKeyAndUserId(@Param("title") String title, @Param("user_id") Integer user_id);

    // 根据 userId 和 musicId 来删除（移除）收藏表中的信息
    Integer deleteLoveMusic(@Param("userid") Integer userid, @Param("musicid") Integer musicid);

    // 根据musicid删除 收藏表中的记录和服务器上的文件
    Integer deleteLoveMusicById(@Param("musicid") Integer musicid);

}


