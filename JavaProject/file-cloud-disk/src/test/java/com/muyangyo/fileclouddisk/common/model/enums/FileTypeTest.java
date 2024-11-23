package com.muyangyo.fileclouddisk.common.model.enums;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/23
 * Time: 11:39
 */
class FileTypeTest {
    public static void main(String[] args) {
        // 通过文件名获取文件类型
        FileType fileType = FileType.fromFileName("document.pdf");
        if (fileType != null) {
            System.out.println("File Type: " + fileType.getTypeName());
            System.out.println("MIME Type: " + fileType.getMimeType());
        } else {
            System.out.println("Unknown file type.");
        }
    }
}