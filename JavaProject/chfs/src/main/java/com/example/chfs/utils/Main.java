package com.example.chfs.utils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // 写入文件
        String filePath = "test.txt";
        String content = "Hello, this is a test file!";
        FileUtils.writeFile(filePath, content, false);

        // 读取文件
        String fileContent = FileUtils.readFile(filePath);
        System.out.println("File content: " + fileContent);

        // 删除文件
        boolean deleted = FileUtils.delete(filePath);
        System.out.println("File deleted: " + deleted);

        // 复制文件
        String copyPath = "copyTest.txt";
        FileUtils.copy(filePath, copyPath);
        System.out.println("File copied to: " + copyPath);

        // 创建目录
        FileUtils.createDirectory("newFolder");

        // 递归删除文件夹
        FileUtils.delete("folderToDelete");

        // 递归复制文件夹
        FileUtils.copy("folderToCopy", "folderCopyDestination");
    }
}
