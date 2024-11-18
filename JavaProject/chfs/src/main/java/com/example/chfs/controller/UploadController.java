package com.example.chfs.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/upload") // 定义上传请求的基础路径
public class UploadController {

    // 上传文件存储的目录
    private static final String UPLOAD_DIR = "C:/Users/DR/Desktop/tmp1";

//    // 上传单个文件的接口
//    @PostMapping("/single") // 定义单文件上传的请求路径
//    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//        // 创建目标文件，将上传的文件保存到指定的目录
//        File targetFile = new File(UPLOAD_DIR + File.separator + file.getOriginalFilename());
//        file.transferTo(targetFile); // 将上传的文件转存到目标位置
//        return "File uploaded successfully: " + targetFile.getName(); // 返回上传成功的信息
//    }

    // 分块上传的接口
    @PostMapping("/chunk") // 定义分块上传的请求路径
    public String uploadChunk(
            @RequestParam("file") MultipartFile file, // 上传的文件
            @RequestParam("chunk") int chunk, // 当前块的索引
            @RequestParam("totalChunks") int totalChunks, // 文件总块数
            @RequestParam("fileName") String fileName, // 文件名
            @RequestParam("md5") String md5 // 文件的MD5哈希
    ) throws IOException {
        // 创建当前块的目标文件
        File targetFile = new File(UPLOAD_DIR + File.separator + fileName + ".part" + chunk);
        file.transferTo(targetFile); // 将当前块文件转存到目标位置

        // 如果当前块是最后一块，合并所有块
        if (chunk == totalChunks - 1) {
            mergeChunks(fileName, totalChunks, md5); // 合并文件块
        }

        return "Chunk uploaded successfully: " + chunk; // 返回当前块上传成功的信息
    }

    // 合并分块文件的方法
    private void mergeChunks(String fileName, int totalChunks, String md5) {
        File[] partFiles = new File[totalChunks]; // 创建文件数组用于存储所有的块文件
        for (int i = 0; i < totalChunks; i++) {
            // 生成每个块的文件对象
            partFiles[i] = new File(UPLOAD_DIR + File.separator + fileName + ".part" + i);
            // 检查分块文件是否存在
            if (!partFiles[i].exists()) {
                throw new RuntimeException("Missing part file: " + partFiles[i].getName()); // 抛出异常
            }
        }

        // 创建合并后的文件对象
        File mergedFile = new File(UPLOAD_DIR + File.separator + fileName);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(mergedFile.toPath()))) {
            // 遍历每个块文件并将其写入合并文件
            for (File partFile : partFiles) {
                try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(partFile.toPath()))) {
                    byte[] buffer = new byte[1024]; // 定义缓冲区
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead); // 写入合并文件
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error during file merge", e); // 抛出合并文件时的异常
        }

        // 删除临时的块文件
        for (File partFile : partFiles) {
            try {
                Files.delete(partFile.toPath()); // 删除块文件
            } catch (IOException e) {
                System.err.println("Error deleting temporary part file: " + partFile.getName()); // 打印删除错误
            }
        }

        // 验证合并后的文件的MD5哈希
        if (!verifyFileMD5(mergedFile, md5)) {
            throw new RuntimeException("File integrity check failed"); // 抛出文件完整性检查失败的异常
        }
    }

    // 验证文件的MD5哈希
    private boolean verifyFileMD5(File file, String expectedMD5) {
        try {
            String actualMD5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(Files.newInputStream(file.toPath()));
            return actualMD5.equals(expectedMD5);
        } catch (IOException e) {
            throw new RuntimeException("Error verifying file MD5", e); // 抛出验证文件MD5时的异常
        }
    }
}