package com.muyangyo.fileclouddisk.user.service;

import com.muyangyo.fileclouddisk.common.component.MountDirComponent;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.exception.IllegalPath;
import com.muyangyo.fileclouddisk.common.model.other.FileType;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.model.vo.FileInfo;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/25
 * Time: 13:25
 */
@Slf4j
@Service
public class FileService {
    private static final int REAL_PATH_INDEX = 0;
    private static final int REAL_MOUNT_ROOT_PATH_INDEX = 1;
    @Resource
    private MountDirComponent mountDirComponent;

    /**
     * 获取指定目录下的文件信息
     *
     * @param path 目录路径
     * @return 文件信息列表
     */
    @SneakyThrows
    public LinkedList<FileInfo> getFileInfoList(String path) {
        if (path.equals(Setting.FE_USER_BASE_URL)) return getRootFileInfo();// 根目录直接返回根目录信息
        // 将path转为绝对路径
        LinkedList<String> linkedList = fakePathConverterToRealPath(path);
        // 遍历目录，获取目录信息
        File file = new File(linkedList.get(REAL_PATH_INDEX));
        List<String> list = FileUtils.listFilesInDirectory(file);
        FileUtils.sortFilePaths(list);

        LinkedList<FileInfo> retList = new LinkedList<>();
        for (String s : list) {
            retList.add(initFileInfo(new File(s), linkedList.get(REAL_MOUNT_ROOT_PATH_INDEX)));
        }
        return retList;
    }

    @SneakyThrows
    public LinkedList<FileInfo> OutSideGetFileInfoList(String path) {
        File file = new File(path);
        List<String> list = FileUtils.listFilesInDirectory(file);
        FileUtils.sortFilePaths(list);

        LinkedList<FileInfo> retList = new LinkedList<>();
        for (String s : list) {
            retList.add(initFileInfo(new File(s), path));
        }
        return retList;
    }

    /**
     * 判断是否在根目录下,并获取文件信息
     *
     * @param file 文件
     * @return 文件信息
     * @throws IllegalPath 路径不在根目录下
     */
    public FileInfo checkFollowRootPathAndGetFileInfo(File file) throws IllegalPath {
        String path = FileUtils.getAbsolutePath(file);
        List<String> dirList = mountDirComponent.getRootMountDir();
        String curRootPath = "";
        boolean ok = false;
        for (String dir : dirList) {
            if (path.equals(dir) || path.startsWith(dir + "/")) {
                curRootPath = dir;
                ok = true;
                break;
            }
        }
        if (!ok) throw new IllegalPath("非法路径!该路径不存在于挂载目录中!");
        return initFileInfo(file, curRootPath);
    }

    /**
     * 判断是否为根目录
     *
     * @param path 路径
     * @return 是否为根目录
     */
    public boolean isRootPath(String path) {
        List<String> dirList = mountDirComponent.getRootMountDir();
        return dirList.contains(path);
    }


    /**
     * 判断是否在根目录下
     *
     * @param file 文件
     * @return 是否在根目录下
     */
    public boolean checkFollowRootPath(File file) {
        String path = FileUtils.getAbsolutePath(file);
        List<String> dirList = mountDirComponent.getRootMountDir();
        boolean ok = false;
        for (String dir : dirList) {
            if (path.equals(dir) || path.startsWith(dir + "/")) {
                ok = true;
                break;
            }
        }
        if (!ok) throw new IllegalPath("非法路径!该路径不存在于挂载目录中!");
        return true;
    }


    /**
     * 预览文件
     *
     * @param path     文件路径
     * @param response 响应
     * @throws IOException IO异常
     */
    public void previewFile(String path, HttpServletResponse response) throws IOException {
        path = FileUtils.normalizePath(path);

        File file = new File(path);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        FileInfo fileInfo = checkFollowRootPathAndGetFileInfo(file);

        response.setContentType(fileInfo.getFileType().getMimeType());
        response.setHeader("Content-Disposition", "inline; filename=" + "temp." + FileUtils.getFileExtension(file));

        try (InputStream inputStream = Files.newInputStream(file.toPath());
             OutputStream outputStream = response.getOutputStream()) {

            byte[] buffer = new byte[Setting.BYTE_CACHE_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }
    }


    /**
     * 获取根目录下的文件信息(私有方法)
     *
     * @return 文件信息列表
     */
    private LinkedList<FileInfo> getRootFileInfo() {
        List<String> dirList = mountDirComponent.getRootMountDir();
        // 遍历目录，获取目录信息

        LinkedList<FileInfo> retList = new LinkedList<>();
        for (String root : dirList) {
            File file = new File(root);
            retList.add(initFileInfo(file, root));
        }
        return retList;
    }

    @SneakyThrows
    public Result createFolder(String parentPath, String folderName) {
        if (parentPath.equals(Setting.FE_USER_BASE_URL)) {
            return Result.fail("根目录不能创建文件夹");
        }

        LinkedList<String> pathList = fakePathConverterToRealPath(parentPath);


        File file = new File(pathList.get(REAL_PATH_INDEX), folderName);

        if (file.exists()) {
            return Result.fail("文件夹已存在");
        }

        if (file.mkdir()) {
            return Result.success("文件夹创建成功");
        } else {
            return Result.fail("文件夹创建失败,可能存在多级目录");
        }
    }

    /**
     * 假路径转换为真路径(私有方法)
     *
     * @param path 假路径
     * @return 包含实际完整路径和实际挂载根目录的链表
     */
    private LinkedList<String> fakePathConverterToRealPath(String path) throws IllegalPath {
        int indexOfSecondSlash = path.indexOf("/", 1);// 第二个斜杠的位置
        path = path.substring(indexOfSecondSlash); // 截取第二个斜杠之后的路径(第一个斜杠的内容是前端用的,后面的才对后端有用)
        String[] strings = path.split("/");
        String mountRootPathFromPath = strings[1];// 挂载根目录的结尾路径

        String realMountRootPath = "";// 实际根目录的结尾路径
        boolean ok = false;// 是否找到了挂载根目录
        List<String> dirList = mountDirComponent.getRootMountDir();
        for (String root : dirList) {
            if (root.endsWith(mountRootPathFromPath)) {
                ok = true;
                realMountRootPath = root;
                break;
            }
        }
        if (!ok) { // 路径不在根目录下
            throw new IllegalPath("非法路径!该路径不存在于挂载目录中!");
        }
        String realPath = realMountRootPath + path.replace("/" + mountRootPathFromPath, "");// 实际路径
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add(realPath);
        linkedList.add(realMountRootPath);
        return linkedList;
    }

    /**
     * 获取指定的文件信息(私有方法)
     *
     * @param file 目录
     * @return 文件信息列表
     */
    @SneakyThrows
    private FileInfo initFileInfo(File file, String mountRootPath) {
        if (mountRootPath.isEmpty()) {
            throw new IllegalPath("挂载根目录为空!");
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(FileUtils.getFileName(file));
        fileInfo.setFileSize(file.isDirectory() ? -1 : file.length());
        fileInfo.setModifiedTime(FileUtils.getFileModifiedTime(file));
        fileInfo.setFilePath(FileUtils.getAbsolutePath(file));
        fileInfo.setMountRootPath(mountRootPath);
        fileInfo.setFileType(FileType.getFileTypeFromFile(file));
        return fileInfo;
    }

    public void uploadChunk(String parentPath, MultipartFile file, int chunkIndex, String fileName, int totalChunks, String chunkMd5) {
        //先将 parentPath 转换为 真路径
        if (parentPath.equals(Setting.FE_USER_BASE_URL)) {
            throw new IllegalPath("根目录不能上传文件");
        }
        LinkedList<String> pathList = fakePathConverterToRealPath(parentPath);
        parentPath = pathList.get(REAL_PATH_INDEX);
        File dest = new File(parentPath, fileName);
        if (dest.exists()) {
            fileName = FileUtils.getFileName(FileUtils.getUniqueFileName(dest.toPath()).toString());
        }


        // 创建当前块的目标文件
        File targetFile = new File(Setting.USER_UPLOAD_TEMP_DIR_PATH + "/" + fileName + ".part" + chunkIndex);
        try {
            if (!new File(Setting.USER_UPLOAD_TEMP_DIR_PATH).exists()) {
                FileUtils.createHiddenDirectory(Setting.USER_UPLOAD_TEMP_DIR_PATH);
            }
            file.transferTo(Paths.get(FileUtils.getAbsolutePath(targetFile))); // 将当前块文件转存到目标位置

            // 检查当前块是否完整
            if (org.apache.commons.codec.digest.DigestUtils.md5Hex(Files.newInputStream(targetFile.toPath())).equals(chunkMd5)) {
                log.info("当前块文件完整: " + targetFile.getName());
                log.info("当前块的MD5值: " + chunkMd5);
            } else {
                throw new RuntimeException("当前块文件不匹配"); // 抛出异常
            }

            // 如果当前块是最后一块，合并所有块
            if (chunkIndex == totalChunks - 1) {
                mergeChunks(fileName, totalChunks, parentPath); // 合并文件块
            }

        } catch (Exception e) {
            try {
                FileUtils.delete(targetFile); // 删除当前块文件
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("上传失败: " + e.getMessage()); // 抛出异常
        }
    }


    // 合并分块文件的方法
    private void mergeChunks(String fileName, int totalChunks, String parentPath) {
        File[] partFiles = new File[totalChunks];
        for (int i = 0; i < totalChunks; i++) {
            // 生成每个块的文件对象
            partFiles[i] = new File(Setting.USER_UPLOAD_TEMP_DIR_PATH + "/" + fileName + ".part" + i);
            // 检查分块文件是否存在
            if (!partFiles[i].exists()) {
                throw new RuntimeException("丢失分块文件: " + partFiles[i].getName()); // 抛出异常
            }
        }

        // 创建合并后的文件对象
        File mergedFile = new File(parentPath + "/" + fileName);
        log.info("开始合并文件: " + fileName);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(mergedFile.toPath()))) {
            // 遍历每个块文件并将其写入合并文件
            for (File partFile : partFiles) {
                try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(partFile.toPath()))) {
                    byte[] buffer = new byte[Setting.BYTE_CACHE_SIZE]; // 定义缓冲区
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead); // 写入合并文件
                        outputStream.flush(); // 刷新缓冲区
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("合并文件失败", e); // 抛出合并文件时的异常
        }

        // 删除临时的块文件
        for (File partFile : partFiles) {
            try {
                Files.delete(partFile.toPath()); // 删除块文件
            } catch (IOException e) {
                log.error("删除分块文件失败: " + partFile.getName()); // 打印删除错误
            }
        }
    }

}
