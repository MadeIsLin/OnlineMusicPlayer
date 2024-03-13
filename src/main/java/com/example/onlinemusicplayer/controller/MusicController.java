package com.example.onlinemusicplayer.controller;


import com.example.onlinemusicplayer.mapper.MusicMapper;
import com.example.onlinemusicplayer.model.Music;
import com.example.onlinemusicplayer.model.User;
import com.example.onlinemusicplayer.service.LoveMusicService;
import com.example.onlinemusicplayer.service.MusicService;
import com.example.onlinemusicplayer.tools.Constant;
import com.example.onlinemusicplayer.tools.ResponseBodyMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/music")
public class MusicController {
    // 存储路径最好是 正斜杠，反斜杠还需要穿衣，有的服务器不识别两个 \\ 的
    @Value("${music.local.path}")
    private String SAVE_PATH;
//    private String SAVE_PATH = "D:/BackEnd/csdn/";

    @Autowired
    MusicService musicService;

    @Autowired
    LoveMusicService loveMusicService;

    @RequestMapping(value = "/upload")
    public ResponseBodyMessage<Boolean> insertMusic(@RequestParam String singer,
                                                    @RequestPart("filename") MultipartFile file,
                                                    HttpServletRequest request) {

        // 首先获取到上传文件的名称
        String fileNameAndType = file.getOriginalFilename(); // 文件名.类型
        // 歌名
        String title = fileNameAndType.substring(0, fileNameAndType.lastIndexOf("."));
        // 查询上传文件是否已经存储过了。
        Integer music = musicService.select(title, singer);
        if (music != null) {
            return new ResponseBodyMessage<>(-1, "此歌曲已上传，请重新选择", false);
        }
        // 存储路径
        String path = SAVE_PATH + fileNameAndType;
        // 封装
        File dest = new File(path);
        // 如果存储路径不存在，则进行创建
        if (!dest.exists()) {
            dest.mkdirs();
        }

        // 指定上传音频文件，上传之后存储的位置
        try {
            file.transferTo(dest);
//            return new ResponseBodyMessage<>(0, "上传成功", true);
        } catch (IOException e) {
            // 当异常被try catch 捕获时，要么抛出异常throw e；要么使用下面的保存点 方法来触发事务的回滚
            // 否则是不会触发事务回滚的
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResponseBodyMessage<>(-1, "上传失败", false);
        }
        // 指定上传音频文件，上传之后存储的位置
//        return new ResponseBodyMessage<>(0, "", false);
        // 进行上传数据库
        // 1.准备要上传的数据
        // 用户id
        User user = (User) request.getSession().getAttribute(Constant.USERINFO_SESSION_KEY);
        Integer userid = user.getId();
        // url -> 播放音乐 -> http请求
        String url = "/music/get?path=" + title;
        // time
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        // 通过Date类来获取当前系统时间，再按照SimpleDateFormat规定日期形式进行转换。
        String time = sf.format(new Date());


        // 2.调用MusicService中的insert方法
        Integer ret = musicService.insert(title, singer, time, url, userid);
        if (ret ==1 ) {
            // 插入成功（上传成功）
            // 其实这里，按照我们的感觉来说，上传成功后，应该跳转歌曲列表页，方便直接看到效果。
            return new ResponseBodyMessage<>(0, "数据库上传成功", true);
        }
        return new ResponseBodyMessage<>(-1, "数据库上传失败", false);
    }

    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(String path) {
//        byte[] a = {97, 98, 99, 100};
//        return ResponseEntity.internalServerError().build();
//        return ResponseEntity.notFound().build();
        // 将读取文件的路径准备好
        File file = new File(SAVE_PATH + path);
        byte[] a = null;
        try {
            a = Files.readAllBytes(file.toPath());
            if (a != null) {
                // 读取成功，将其返回给前端
                return ResponseEntity.ok(a);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 读取失败
        return ResponseEntity.badRequest().build();
    }


    @RequestMapping(value = "/delete")
    public ResponseBodyMessage<Boolean> delete(@RequestParam(required = false) Integer id) {
        if (id == null) {
            return new ResponseBodyMessage<>(-1, "无效id", false);
        }
        // 检查 删除歌曲是否在数据库中存在
        Music music = musicService.selectMusicById(id);
        if (music == null) {
            return new ResponseBodyMessage<>(-1, "你要删除的歌曲不存在，请重新选择", false);
        }
        // 根据id删除指定的歌曲信息
        Integer ret = musicService.deleteMusicById(id);
        if (ret == 0) {
            return new ResponseBodyMessage<>(-1, "删除失败", false);
        }

        // 这里还需要删除，服务器上存储的歌曲数据。
        // 获取文件名，例如：/music/get?path=歌名
        int index = music.getUrl().lastIndexOf("=");
        String fileName = music.getUrl().substring(index + 1); // 截取歌名
        File file = new File(SAVE_PATH + fileName + ".mp3");
        if (file.delete()) {
            loveMusicService.deleteLoveMusicById(id);
            return new ResponseBodyMessage<>(0, "删除成功", true);
        }
        return new ResponseBodyMessage<>(-1, "删除失败", false);
    }

    @RequestMapping(value = "deletesel")
    @Transactional
    public ResponseBodyMessage<Boolean> deleteSelMusic(@RequestParam("ids[]")List<Integer> ids) {
        int sum = 0;
        for (int i = 0; i < ids.size(); i++) {
            // 查询歌曲是否存在
            Music music = musicService.selectMusicById(ids.get(i));
            if (music == null) {
                // 歌曲不存在，默认删除成功
                sum+=1;
                continue;
            }
            int ret = musicService.deleteMusicById(ids.get(i));
            if (ret == 1) {
                // 获取文件名，例如: /music/get?path=歌名
                int index = music.getUrl().lastIndexOf("=");
                String fileName = music.getUrl().substring(index+1); // 截取歌名
                File file = new File(SAVE_PATH + fileName + ".mp3");
                if (file.delete()) {
                    sum++;
                    loveMusicService.deleteLoveMusicById(ids.get(i));
                    continue;
                }
                // 走到这一步说明服务器删除文件失败，那么相应的事务操作，需要进行回滚。
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                // 返回 删除失败的信息，以及当前成功删除了多少首歌。
                return new ResponseBodyMessage<>(-1, "批量删除失败，当前成功删除了" + sum + "首歌", false);

            }
            return new ResponseBodyMessage<>(-1, "批量删除失败，当前成功删除了" + sum + "首歌", false);
        }
        return new ResponseBodyMessage<>(0, "批量删除成功，一个删除了" + sum + "首歌", true);
    }

    @RequestMapping(value = "/findmusic")
    public ResponseBodyMessage<List<Music>> findMusic(@RequestParam(required = false) String name) {
        List<Music> list = null;
        if (name == null) {
            list = musicService.findByMusicByName();
        } else {
            list = musicService.findByMusicByName(name);
        }
        return new ResponseBodyMessage<>(0, "查询到了歌曲信息", list);
    }


}
