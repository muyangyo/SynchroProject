package com.example.chfs.model;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/24
 * Time: 13:31
 */
@Data
public class FileForList {
    private String name;
    private boolean isDirectory;
    private String path;

    public FileForList(String name, boolean isDirectory, String path) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.path = path;
    }
}
