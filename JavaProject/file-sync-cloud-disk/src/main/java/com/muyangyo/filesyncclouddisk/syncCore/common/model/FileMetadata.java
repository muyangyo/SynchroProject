package com.muyangyo.filesyncclouddisk.syncCore.common.model;

/**
 * 文件元数据类
 */
public class FileMetadata {
    String path; // 文件路径
    long size;// 文件大小
    long lastModified; // 文件最后修改时间
    long checksum; // 文件校验码

    public FileMetadata(String path, long size, long lastModified, long checksum) {
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.checksum = checksum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return size == that.size &&
                lastModified == that.lastModified &&
                checksum == that.checksum;
    }
}