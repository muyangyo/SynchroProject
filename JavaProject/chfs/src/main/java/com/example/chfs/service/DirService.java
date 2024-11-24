package com.example.chfs.service;

import com.example.chfs.model.FileForList;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/24
 * Time: 13:30
 */
@Service
public class DirService {
    public List<FileForList> listFiles(String path) {
        List<FileForList> fileInfos = new ArrayList<>();
        File directory = new File(path);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    fileInfos.add(new FileForList(file.getName(), file.isDirectory(), file.getAbsolutePath()));
                }
            }
        }

        return fileInfos;
    }
}
