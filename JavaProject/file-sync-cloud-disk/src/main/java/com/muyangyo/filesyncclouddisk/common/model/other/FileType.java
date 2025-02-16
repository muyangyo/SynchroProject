package com.muyangyo.filesyncclouddisk.common.model.other;

import com.muyangyo.filesyncclouddisk.common.model.enums.FileCategory;
import com.muyangyo.filesyncclouddisk.common.model.enums.SubdividedFileType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.File;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/25
 * Time: 20:36
 */
@Data
@NoArgsConstructor
public class FileType {
    @NonNull
    private FileCategory category;//所属分类
    @NonNull
    private String typeName;//类型名称
    @NonNull
    private String mimeType;//mime类型

    public static FileType getFileTypeFromFile(File file) {
        FileType fileType = new FileType();

        SubdividedFileType subdividedType = SubdividedFileType.fromFileName(file);
        fileType.category = subdividedType.getCategory();
        fileType.typeName = subdividedType.getTypeName();
        fileType.mimeType = subdividedType.getMimeType();
        return fileType;
    }
}
