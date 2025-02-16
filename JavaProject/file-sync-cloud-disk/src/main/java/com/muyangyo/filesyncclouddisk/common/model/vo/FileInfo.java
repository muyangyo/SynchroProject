package com.muyangyo.filesyncclouddisk.common.model.vo;

import com.muyangyo.filesyncclouddisk.common.model.other.FileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/25
 * Time: 13:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private String fileName;
    private long fileSize;
    private String modifiedTime;
    private String filePath;
    private String mountRootPath; // 属于哪个挂载路径
    private FileType fileType;
}
