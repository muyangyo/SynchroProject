package com.example.scdemo231128.demo2;

import ch.qos.logback.core.util.FileUtil;
import com.example.scdemo231128.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/29
 * Time: 12:19
 */
@RequestMapping("/file")
@Slf4j
@RestController
public class FileReceive {

    @Autowired
    FileMapper fileMapper;

    @RequestMapping("/receive")
    public String fileRec(@RequestPart(required = false) MultipartFile fileUpload) {
        if (fileUpload == null) return "上传失败";
        String fileName = fileUpload.getOriginalFilename();
        log.info(fileName + " 大小为: " + fileUpload.getSize() + "byte");
        // 保存文件到指定目录
        File dest = new File("A:/SynchroProject/JavaProject/scdemo231128/file/" + fileName);
        try {
            fileUpload.transferTo(dest);
            log.info(dest.getAbsolutePath());
            fileMapper.insert(fileName, dest.getAbsolutePath());
            return "上传成功";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
