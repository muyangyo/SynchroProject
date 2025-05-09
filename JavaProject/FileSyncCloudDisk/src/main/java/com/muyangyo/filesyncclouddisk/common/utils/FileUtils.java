package com.muyangyo.filesyncclouddisk.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException           如果读取过程中发生错误
     * @throws FileNotFoundException 如果文件不存在
     */
    public static String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("文件不存在: " + filePath);
        }
        // 这里使用指定编码读取文件内容
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    /**
     * 写入内容到文件
     *
     * @param filePath 文件路径 (如果文件或者文件夹不存在，则会自动创建)
     * @param content  写入内容 (默认写入为UTF-8编码)
     * @param append   是否追加内容，true: 追加，false: 覆盖
     * @throws IOException 如果写入过程中发生错误
     */
    public static void writeFile(String filePath, String content, boolean append) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent()); // 创建文件夹
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path,
                StandardOpenOption.CREATE,
                append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING)) {
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
     * 复制文件
     *
     * @param sourcePath 源文件路径
     * @param targetPath 目标文件路径
     * @throws IOException 如果复制过程中发生错误
     */
    public static void copyFile(String sourcePath, String targetPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);

        if (!Files.exists(source)) {
            throw new FileNotFoundException("源文件不存在: " + sourcePath);
        }
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 创建文件夹(可多级创建)
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
     * 创建隐藏文件夹
     *
     * @param dirPath 隐藏文件夹路径
     * @throws IOException 如果创建过程中发生错误
     */
    public static void createHiddenDirectory(String dirPath) throws IOException {
        createDirectory(dirPath); // 在Unix/Linux系统中，通过在目录前加“.”来创建隐藏文件夹即可
        Path path = Paths.get(dirPath);
        // 如果是Windows系统的话，还要将目录属性设置为隐藏
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            Files.setAttribute(path, "dos:hidden", true);
        }
    }

    /**
     * 列出当前目录下所有文件和文件夹(不含子文件夹的文件)
     *
     * @param dirPath 目录路径
     * @return 文件列表
     * @throws IOException 如果获取文件列表过程中发生错误
     */
    public static List<String> listFilesInDirectory(String dirPath) throws IOException {
        Path dir = Paths.get(dirPath);
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("指定的路径不是一个有效的目录: " + dirPath);
        }

        List<String> fileList = new LinkedList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                fileList.add(entry.toString());
            }
        }
        return fileList;
    }

    /**
     * 列出当前目录及其子目录下所有文件和文件夹
     *
     * @param dirPath 目录路径
     * @return 文件列表
     * @throws IOException 如果获取文件列表过程中发生错误
     */
    public static List<String> listAllFilesInDirectory(String dirPath) throws IOException {
        if (dirPath == null || dirPath.isEmpty()) {
            throw new IllegalArgumentException("目录路径不能为 null 或空字符串");
        }

        Path dir = Paths.get(dirPath);
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            throw new IllegalArgumentException("指定的路径不是一个有效的目录: " + dirPath);
        }

        List<String> fileList = new LinkedList<>();
        try (Stream<Path> stream = Files.walk(dir)) {
            fileList = stream
                    .map(Path::toString)
                    .collect(Collectors.toList());
        }
        fileList.remove(dirPath); // 移除根目录路径
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
        String fileName = Paths.get(filePath).getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 获取文件名（含扩展名）
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        return Paths.get(filePath).getFileName().toString();
    }

    /**
     * 获取文件名（不含扩展名）
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileNameWithoutExtension(String filePath) {
        String fileName = getFileName(filePath);
        int lastDotIndex = fileName.lastIndexOf('.'); // 查找最后一个"."的位置
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex); // 返回文件名（不含扩展名）
        }
        return fileName;
    }

    /**
     * 将文件传输到目标路径，并根据用户选择处理文件已存在的情况
     *
     * @param sourceFile     源文件
     * @param targetFilePath 目标文件路径 (非文件夹)
     * @param overwrite      是否覆盖目标文件，true: 覆盖 ，false: 不覆盖
     * @throws IOException 如果传输过程中发生错误
     */
    public static void saveFile(File sourceFile, String targetFilePath, boolean overwrite) throws IOException {
        Path sourcePath = sourceFile.toPath();
        Path targetPath = Paths.get(targetFilePath);

        // 如果目标文件夹不存在，创建它
        if (!Files.exists(targetPath.getParent())) {
            Files.createDirectories(targetPath.getParent());
        }

        // 如果目标文件已存在
        if (Files.exists(targetPath)) {
            if (overwrite) {
                // 覆盖目标文件
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                // 在文件名后添加数字以避免覆盖
                targetPath = getUniqueFileName(targetPath);
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            // 目标文件不存在，直接传输
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * 获取唯一的文件名，避免覆盖
     *
     * @param originalPath 原始文件路径
     * @return 唯一的文件路径
     */
    private static Path getUniqueFileName(Path originalPath) {
        Path parent = originalPath.getParent(); // 获取父目录
        String fileName = originalPath.getFileName().toString(); // 获取文件名
        String baseName = fileName; // 文件名（不含扩展名）
        String extension = ""; // 文件扩展名

        int dotIndex = fileName.lastIndexOf('.'); // 查找最后一个"."的位置
        if (dotIndex > 0) {
            baseName = fileName.substring(0, dotIndex); // 获取文件名（不含扩展名）
            extension = fileName.substring(dotIndex); // 获取文件扩展名
        }

        int counter = 1;
        Path uniquePath = originalPath;
        while (Files.exists(uniquePath)) {
            uniquePath = parent.resolve(baseName + "(" + counter + ")" + extension);
            counter++;
        }

        return uniquePath;
    }
}
