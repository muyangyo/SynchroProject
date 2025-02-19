package com.muyangyo.filesyncclouddisk.syncCore.common.model;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件元数据类
 */
@Data
@NoArgsConstructor
public class FileMetadata {
    String path; // 文件路径
    long size;// 文件大小
    long lastModified; // 文件最后修改时间
    long crc32; // 文件校验码

    public FileMetadata(String path, long size, long lastModified, long crc32) {
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.crc32 = crc32;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return size == that.size &&
//                lastModified == that.lastModified && // 忽略 lastModified 时间差异,只要改了CRC32就认为文件不同
                crc32 == that.crc32;
    }
}