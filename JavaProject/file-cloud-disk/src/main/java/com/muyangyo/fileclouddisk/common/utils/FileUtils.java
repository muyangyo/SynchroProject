package com.muyangyo.fileclouddisk.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     * @return 文件内容
     * @throws IOException           如果读取过程中发生错误
     * @throws FileNotFoundException 如果文件不存在
     */
    public static String readFileToStr(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("文件不存在: " + filePath);
        }
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    /**
     * 读取文件内容
     *
     * @param file 文件对象
     * @return 文件内容
     * @throws IOException           如果读取过程中发生错误
     * @throws FileNotFoundException 如果文件不存在
     */
    public static String readFileToStr(File file) throws IOException {
        return readFileToStr(file.getPath());
    }

    /**
     * 写入内容到文件
     *
     * @param filePath 文件路径 (如果文件或者文件夹不存在，则会自动创建)
     * @param content  写入内容 (默认写入为UTF-8编码)
     * @param append   是否追加内容，true: 追加，false: 覆盖
     * @throws IOException 如果写入过程中发生错误
     */
    public static void writeFileFromStr(String filePath, String content, boolean append) throws IOException {
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
     * 写入内容到文件
     *
     * @param file    文件对象
     * @param content 写入内容 (默认写入为UTF-8编码)
     * @param append  是否追加内容，true: 追加，false: 覆盖
     * @throws IOException 如果写入过程中发生错误
     */
    public static void writeFileFromStr(File file, String content, boolean append) throws IOException {
        writeFileFromStr(file.getPath(), content, append);
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
     * 删除文件或目录（递归删除文件夹及其内容）
     *
     * @param file 文件或目录对象
     * @return 删除成功或失败
     * @throws IOException 如果删除过程中发生错误
     */
    public static boolean delete(File file) throws IOException {
        return delete(file.getPath());
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
     * 复制文件
     *
     * @param sourceFile 源文件对象
     * @param targetFile 目标文件对象
     * @throws IOException 如果复制过程中发生错误
     */
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        copyFile(sourceFile.getPath(), targetFile.getPath());
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
     * 创建文件夹(可多级创建)
     *
     * @param dir 文件夹对象
     * @throws IOException 如果创建过程中发生错误
     */
    public static void createDirectory(File dir) throws IOException {
        createDirectory(dir.getPath());
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
     * 创建隐藏文件夹
     *
     * @param dir 隐藏文件夹对象
     * @throws IOException 如果创建过程中发生错误
     */
    public static void createHiddenDirectory(File dir) throws IOException {
        createHiddenDirectory(dir.getPath());
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
     * 列出当前目录下所有文件和文件夹(不含子文件夹的文件)
     *
     * @param dir 目录对象
     * @return 文件列表
     * @throws IOException 如果获取文件列表过程中发生错误
     */
    public static List<String> listFilesInDirectory(File dir) throws IOException {
        return listFilesInDirectory(dir.getPath());
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
     * 列出当前目录及其子目录下所有文件和文件夹
     *
     * @param dir 目录对象
     * @return 文件列表
     * @throws IOException 如果获取文件列表过程中发生错误
     */
    public static List<String> listAllFilesInDirectory(File dir) throws IOException {
        return listAllFilesInDirectory(dir.getPath());
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
     * 检查文件或文件夹是否存在
     *
     * @param file 文件或文件夹对象
     * @return 是否存在
     */
    public static boolean exists(File file) {
        return exists(file.getPath());
    }

    /**
     * 获取文件的扩展名
     *
     * @param filePath 文件路径
     * @return 文件扩展名
     */
    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            return filePath.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 获取文件的扩展名
     *
     * @param file 文件对象
     * @return 文件扩展名
     */
    public static String getFileExtension(File file) {
        return getFileExtension(file.getPath());
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
     * 获取文件名（含扩展名）
     *
     * @param file 文件对象
     * @return 文件名
     */
    public static String getFileName(File file) {
        return getFileName(file.getPath());
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
     * 获取文件名（不含扩展名）
     *
     * @param file 文件对象
     * @return 文件名
     */
    public static String getFileNameWithoutExtension(File file) {
        return getFileNameWithoutExtension(file.getPath());
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

    /**
     * 获取文件的修改时间
     *
     * @param filePath 文件路径
     * @return 文件的修改时间
     * @throws IOException 如果获取过程中发生错误
     */
    public static String getFileModifiedTime(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        Date modifiedTime = new Date(attrs.lastModifiedTime().toMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(modifiedTime);
    }

    /**
     * 获取文件的修改时间
     *
     * @param file 文件对象
     * @return 文件的修改时间
     * @throws IOException 如果获取过程中发生错误
     */
    public static String getFileModifiedTime(File file) throws IOException {
        return getFileModifiedTime(file.getPath());
    }

    /**
     * 将文件路径中的反斜杠 \ 替换为正斜杠 /
     *
     * @param path 文件路径
     * @return 替换后的路径
     */
    public static String normalizePath(String path) {
        // 先将反斜杠替换为正斜杠
        String normalizedPath = path.replace("\\", "/");
        // 删除重复的斜杠
        return normalizedPath.replaceAll("/+", "/");
    }

    /**
     * 获取绝对路径(分割符统一为 "/",避免了与不同操作系统的路径分割符不同导致的路径错误)
     *
     * @param path 文件路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path) {
        return normalizePath(Paths.get(path).toAbsolutePath().toString());
    }

    /**
     * 获取绝对路径(分割符统一为 "/",避免了与不同操作系统的路径分割符不同导致的路径错误)
     *
     * @param file 文件对象
     * @return 绝对路径
     */
    public static String getAbsolutePath(File file) {
        return getAbsolutePath(file.getPath());
    }

    /**
     * 对文件路径列表进行排序，文件夹排在前面，然后根据文件扩展名排序
     *
     * @param filePaths 文件路径列表
     */
    public static void sortFilePaths(List<String> filePaths) {
        filePaths.sort((path1, path2) -> {
            boolean isDir1 = new File(path1).isDirectory();
            boolean isDir2 = new File(path2).isDirectory();

            // 文件夹排在前面
            if (isDir1 && !isDir2) {
                return -1;
            } else if (!isDir1 && isDir2) {
                return 1;
            }

            // 如果都是文件夹或都是文件，则根据文件扩展名排序
            String ext1 = getFileExtension(path1);
            String ext2 = getFileExtension(path2);

            return ext1.compareTo(ext2);
        });
    }

    /**
     * 对文件对象列表进行排序，文件夹排在前面，然后根据文件扩展名排序
     *
     * @param files 文件对象列表
     */
    public static void sortFiles(List<File> files) {
        files.sort((file1, file2) -> {
            boolean isDir1 = file1.isDirectory();
            boolean isDir2 = file2.isDirectory();

            // 文件夹排在前面
            if (isDir1 && !isDir2) {
                return -1;
            } else if (!isDir1 && isDir2) {
                return 1;
            }

            // 如果都是文件夹或都是文件，则根据文件扩展名排序
            String ext1 = getFileExtension(file1);
            String ext2 = getFileExtension(file2);

            return ext1.compareTo(ext2);
        });
    }

    /**
     * 重命名文件或目录(只能在同一目录下重命名)
     *
     * @param oldPath 原文件或目录路径
     * @param newName 新文件或目录名称
     * @return 重命名成功或失败
     * @throws IOException 如果重命名过程中发生错误
     */
    public static boolean rename(String oldPath, String newName) throws IOException {
        Path oldFilePath = Paths.get(oldPath);
        if (!Files.exists(oldFilePath)) {
            throw new FileNotFoundException("文件或目录不存在: " + oldPath);
        }

        // 获取父目录路径
        Path parentDir = oldFilePath.getParent();
        if (parentDir == null) {
            throw new IllegalArgumentException("无法获取父目录路径: " + oldPath);
        }

        // 构建新的文件路径
        Path newFilePath = parentDir.resolve(newName);

        // 检查新路径是否与原路径相同
        if (oldFilePath.equals(newFilePath)) {
            return true; // 如果新旧路径相同，直接返回成功
        }

        // 检查新路径是否已经存在，如果存在则获取唯一的文件名
        if (Files.exists(newFilePath)) {
            newFilePath = getUniqueFileName(newFilePath);
        }

        // 确保新路径与原路径在同一个目录下
        if (!parentDir.equals(newFilePath.getParent())) {
            throw new IllegalArgumentException("新文件路径不能在不同的目录下: " + newFilePath);
        }

        // 执行重命名操作
        Files.move(oldFilePath, newFilePath);
        return true;
    }

    /**
     * 重命名文件或目录(只能在同一目录下重命名)
     *
     * @param oldFile 原文件或目录对象
     * @param newName 新文件或目录名称
     * @return 重命名成功或失败
     * @throws IOException 如果重命名过程中发生错误
     */
    public static boolean rename(File oldFile, String newName) throws IOException {
        return rename(oldFile.getPath(), newName);
    }

    /**
     * 压缩文件或目录
     *
     * @param sourceDirPath 源文件或目录路径
     * @param destZipFile   目标 ZIP 文件对象（必须不存在）
     * @throws IOException 如果压缩过程中发生错误
     */
    public static void zip(String sourceDirPath, File destZipFile) throws IOException {
        File sourceDir = new File(sourceDirPath);

        // 检查源文件或目录是否存在
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IOException("源文件或目录不存在: " + sourceDirPath);
        }

        // 检查目标 ZIP 文件是否存在
        if (destZipFile.exists()) {
            throw new IOException("目标 ZIP 文件已存在: " + destZipFile.getPath());
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destZipFile))) {
            compress(sourceDir, zos, sourceDir.getName());
        }
    }

    private static final int BUFFER_SIZE = 1024;
    /**
     * 递归压缩方法
     *
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @throws IOException 如果压缩过程中发生错误
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            try (FileInputStream in = new FileInputStream(sourceFile)) {
                int len;
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
            }
            // Complete the entry
            zos.closeEntry();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 空文件夹的处理
                zos.putNextEntry(new ZipEntry(name + "/"));
                zos.closeEntry();
            } else {
                for (File file : listFiles) {
                    // 递归压缩子文件或子目录
                    compress(file, zos, name + "/" + file.getName());
                }
            }
        }
    }
}