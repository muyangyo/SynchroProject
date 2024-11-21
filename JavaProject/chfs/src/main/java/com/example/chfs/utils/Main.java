package com.example.chfs.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static com.example.chfs.utils.FileUtils.*;


public class Main {
    public static final String filePath = "C:\\Users\\DR\\Desktop\\imitate-baidupan-main.7z";
    public static final String dirPath = "C:\\Users\\DR\\Desktop\\tmp1\\1/2/3/4/5";
    public static final String hiddenDirPath = "C:\\Users\\DR\\Desktop\\tmp1/.hiddenDir";


    public static void main5(String[] args) throws IOException {
        saveFile(new File("C:\\Users\\DR\\Desktop\\分散实习申请表.docx"), "C:\\Users\\DR\\Desktop\\tmp1\\分散实习申请表.docx", false);
    }

    public static void main4(String[] args) {
        System.out.println(getFileNameWithoutExtension(filePath));
        System.out.println(getFileName(filePath));
        System.out.println(getFileExtension(filePath));
    }

    public static void main3(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        List<String> list = FileUtils.listFilesInDirectory("A:\\SynchroProject");
        System.out.println(list.size());
        System.out.println(list);
        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start) + "ms");
    }

    public static void main2(String[] args) throws IOException {
        File targetFile = new File(filePath);
        String s = DigestUtils.md5Hex(Files.newInputStream(targetFile.toPath()));
        System.out.println(s);
    }

    public static void main1(String[] args) throws IOException {
        // 读取文件内容
        String content = FileUtils.readFile(filePath);
        System.out.println("读取文件内容: " + content);

        // 写入内容到文件
        FileUtils.writeFile(filePath, "Hello, FileUtils!", true);
        System.out.println("文件写入成功: " + filePath);

        // 创建目录
        FileUtils.createDirectory(dirPath);
        System.out.println("目录创建成功: " + dirPath);

        //复制文件
        FileUtils.copyFile("C:\\Users\\DR\\Desktop\\tmp1\\新建文件夹\\1\\说明.txt", "C:\\Users\\DR\\Desktop\\tmp1\\1\\新建文件夹/1.txt");


        // 列出目录下所有文件
        System.out.println("列出目录文件: " + FileUtils.listFilesInDirectory("C:\\Users\\DR\\Desktop\\tmp1"));

        // 删除文件
        boolean deleted = FileUtils.delete("C:\\Users\\DR\\Desktop\\tmp1\\新建文件夹\\说明.txt");

//
        // 创建隐藏文件夹
        FileUtils.createHiddenDirectory(hiddenDirPath);
        System.out.println("隐藏文件夹创建成功: " + hiddenDirPath);

        //检查文件或文件夹是否存在
        boolean exists = FileUtils.exists("C:\\Users\\DR\\Desktop\\tmp1\\分散实习安全承诺书.docx");
        System.out.println("文件是否存在: " + exists);

        // 获取文件扩展名
        String extension = FileUtils.getFileExtension("C:\\Users\\DR\\Desktop\\tmp1\\分散实习安全承诺书.docx");
        System.out.println("扩展名: " + extension);
    }
}
