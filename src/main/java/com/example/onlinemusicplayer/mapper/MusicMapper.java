package com.example.onlinemusicplayer.mapper;

import com.example.onlinemusicplayer.model.Music;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface MusicMapper {
    // 上传歌曲（向music表中插入一条数据）
    Integer insert(@Param("title") String title, @Param("singer") String singer,
                   @Param("time") String time, @Param("url") String url,
                   @Param("userid") Integer userid);

    // 检查上传歌曲是否已经再数据库存在
    Integer select(@Param("title") String title, @Param("singer") String singer);


    // 查询当前要删除的音乐是否在数据库中存在
    Music selectMusicById(@Param("id") Integer id);

    // 删除指定id的音乐
    Integer deleteMusicById(@Param("id") Integer id);


    // 查询音乐：模糊查询：查询结果 1-n 首歌
    List<Music> findByMusicByName(@Param("name") String name);

    // 查询所有音乐
    List<Music> findByMusicByName();


}
