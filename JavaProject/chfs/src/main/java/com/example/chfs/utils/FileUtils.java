package com.example.chfs.utils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException 如果读取过程中发生错误
     */
    public static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        return new String(Files.readAllBytes(path));
    }

    /**
     * 写入内容到文件
     *
     * @param filePath 文件路径
     * @param content  写入内容
     * @param append   是否追加内容，true: 追加，false: 覆盖
     * @throws IOException 如果写入过程中发生错误
     */
    public static void writeFile(String filePath, String content, boolean append) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent()); // 创建文件夹
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(content);
        }
    }

    /**
     * 删除文件或目录（递归删除文件夹及其内容）
     *
     * @param path 文件或目录路径
     * @return 删除成功或失败
     * @throws IOException 如果删除过程中发生错误
     */
    public static boolean delete(String path) throws IOException {
        Path targetPath = Paths.get(path);
        if (Files.exists(targetPath)) {
            if (Files.isDirectory(targetPath)) {
                // 递归删除目录内容
                Files.walkFileTree(targetPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir); // 删除目录
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                // 如果是文件直接删除
                Files.delete(targetPath);
            }
            return true;
        }
        return false;
    }

    /**
     * 复制文件或目录（递归复制文件夹及其内容）
     *
     * @param sourcePath  源文件或目录路径
     * @param targetPath  目标文件或目录路径
     * @throws IOException 如果复制过程中发生错误
     */
    public static void copy(String sourcePath, String targetPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);

        if (!Files.exists(source)) {
            throw new FileNotFoundException("Source file or directory not found: " + sourcePath);
        }

        if (Files.isDirectory(source)) {
            // 如果是目录，递归复制
            Files.createDirectories(target); // 确保目标目录存在
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = target.resolve(source.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Path targetDir = target.resolve(source.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            // 如果是文件直接复制
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * 创建文件夹
     *
     * @param dirPath 文件夹路径
     * @throws IOException 如果创建过程中发生错误
     */
    public static void createDirectory(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /**
     * 列出目录下所有文件
     *
     * @param dirPath 目录路径
     * @return 文件列表
     * @throws IOException 如果获取文件列表过程中发生错误
     */
    public static List<String> listFilesInDirectory(String dirPath) throws IOException {
        List<String> fileList = new ArrayList<>();
        Path dir = Paths.get(dirPath);
        if (Files.exists(dir) && Files.isDirectory(dir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path entry : stream) {
                    fileList.add(entry.toString());
                }
            }
        }
        return fileList;
    }

    /**
     * 检查文件或文件夹是否存在
     *
     * @param path 文件或文件夹路径
     * @return 是否存在
     */
    public static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }

    /**
     * 获取文件的扩展名
     *
     * @param filePath 文件路径
     * @return 文件扩展名
     */
    public static String getFileExtension(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
}
