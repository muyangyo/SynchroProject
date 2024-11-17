package com.example.chfs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    private String name;
    private String path;
    private long size;
    private boolean isDirectory;

    public FileInfo(String name, String path, long size) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.isDirectory = size == -1;
    }
}