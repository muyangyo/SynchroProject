package com.muyangyo.filesyncclouddisk.user.service;

import cn.hutool.core.util.RandomUtil;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.exception.IllegalPath;
import com.muyangyo.filesyncclouddisk.common.mapper.ShareFolderMapper;
import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.filesyncclouddisk.common.model.meta.RecycleBinFile;
import com.muyangyo.filesyncclouddisk.common.model.other.FileType;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.model.vo.FileInfo;
import com.muyangyo.filesyncclouddisk.common.model.vo.RecycleBinFileVO;
import com.muyangyo.filesyncclouddisk.common.utils.EasyTimer;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import com.muyangyo.filesyncclouddisk.common.utils.NetworkUtils;
import com.muyangyo.filesyncclouddisk.manager.service.OperationLogService;
import com.muyangyo.filesyncclouddisk.user.mapper.RecycleBinFileMapper;
import converter.MetaToVoConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
    private OperationLogService operationLogService;
    @Resource
    private RecycleBinFileMapper recycleBinFileMapper;

    @Resource
    private ShareFolderMapper shareFolderMapper;

    /**
     * 获取指定目录下的文件信息
     *
     * @param path    目录路径
     * @param request
     * @return 文件信息列表
     */
    @SneakyThrows
    public LinkedList<FileInfo> getFileInfoList(String path, HttpServletRequest request) {
        operationLogService.addLogFromRequest("获取文件列表", OperationLevel.INFO, request);
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
    public LinkedList<FileInfo> OutSideGetFileInfoList(String path, HttpServletRequest request) {
        File file = new File(path);
        List<String> list = FileUtils.listFilesInDirectory(file);
        FileUtils.sortFilePaths(list);

        LinkedList<FileInfo> retList = new LinkedList<>();
        for (String s : list) {
            retList.add(initFileInfo(new File(s), path));
        }
        operationLogService.addLog("外部用户获取文件列表", "unknown", NetworkUtils.getClientIp(request), OperationLevel.INFO);
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
        List<String> dirList = shareFolderMapper.getShareFolderList();
        ;
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
        List<String> dirList = shareFolderMapper.getShareFolderList();
        ;
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
        List<String> dirList = shareFolderMapper.getShareFolderList();
        ;
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
     * @param request
     * @throws IOException IO异常
     */
    public void previewFile(String path, HttpServletResponse response, HttpServletRequest request) throws IOException {
        path = FileUtils.normalizePath(path);

        File file = new File(path);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        FileInfo fileInfo = checkFollowRootPathAndGetFileInfo(file);

        operationLogService.addLogFromRequest("预览文件", OperationLevel.INFO, request);

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
        List<String> dirList = shareFolderMapper.getShareFolderList();
        ;
        // 遍历目录，获取目录信息

        LinkedList<FileInfo> retList = new LinkedList<>();
        for (String root : dirList) {
            File file = new File(root);
            retList.add(initFileInfo(file, root));
        }
        return retList;
    }

    @SneakyThrows
    public Result createFolder(String parentPath, String folderName, HttpServletRequest request) {
        if (parentPath.equals(Setting.FE_USER_BASE_URL)) {
            return Result.fail("根目录不能创建文件夹");
        }

        LinkedList<String> pathList = fakePathConverterToRealPath(parentPath);


        File file = new File(pathList.get(REAL_PATH_INDEX), folderName);

        if (file.exists()) {
            return Result.fail("文件夹已存在");
        }

        if (file.mkdir()) {
            operationLogService.addLogFromRequest("创建文件夹", OperationLevel.WARNING, request);
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
        List<String> dirList = shareFolderMapper.getShareFolderList();
        ;
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
        String realPath = realMountRootPath + path.replaceFirst("/" + mountRootPathFromPath, "");// 实际路径
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

    public void uploadChunk(String parentPath, MultipartFile file, int chunkIndex, String fileName, int totalChunks, String chunkMd5, HttpServletRequest request) {
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
                log.trace("当前块文件完整: " + targetFile.getName());
                log.trace("当前块的MD5值: " + chunkMd5);
            } else {
                throw new RuntimeException("当前块文件不匹配"); // 抛出异常
            }

            // 如果当前块是最后一块，合并所有块
            if (chunkIndex == totalChunks - 1) {
                mergeChunks(fileName, totalChunks, parentPath); // 合并文件块
            }
            operationLogService.addLogFromRequest("上传文件 [" + targetFile.getName() + "]", OperationLevel.WARNING, request);

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
        log.trace("开始合并文件: " + fileName);
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
        log.trace("文件合并成功,并成功删除分块文件");
    }


    /**
     * 下载文件(支持多线程)
     *
     * @param file     文件
     * @param request  请求
     * @param response 响应
     */
    @SneakyThrows
    public void downloadFile(File file, HttpServletRequest request, HttpServletResponse response) {
        // 下载文件
        long fileSize = file.length();
        long start = 0, end = fileSize - 1; // 默认下载全部(浏览器)

        String rangeHeader = request.getHeader(HttpHeaders.RANGE);
        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) { // 如果是range 请求
            String[] ranges = rangeHeader.substring("bytes=".length()).split("-");
            start = Long.parseLong(ranges[0]); // 开始位置
            end = ranges.length > 1 && !ranges[1].isEmpty() ? Long.parseLong(ranges[1]) : fileSize - 1; // 结束位置

            // 检查 Range 是否有效
            if (start < 0 || end >= fileSize || start > end) {
                response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }
        }

        long count = end - start + 1;// 计算传输的长度

        // 这个是避免文件名是特殊字符
        String fileName = file.getName();
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name())
                .replace("+", "%20");

        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/octet-stream"); // 设置响应类型
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + encodedFileName);
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(count)); // 设置响应长度

        // 如果是range 请求,则设置 range 响应头
        if (start > 0 || end < fileSize - 1) {
            response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize);
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        }

        // 使用 FileChannel 和 transferTo 方法高效传输文件内容
        try (FileChannel fileChannel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
             OutputStream outputStream = response.getOutputStream()) {
            // 使用 transferTo 方法直接传输文件内容
            fileChannel.transferTo(start, count, Channels.newChannel(outputStream));
        } catch (IOException e) {
            if (e.getMessage().contains("远程主机强迫关闭了一个现有的连接") || e.getMessage().contains("你的主机中的软件中止了一个已建立的连接")) {
                log.warn("由于多线程下载文件导致主机强迫关闭了一个现有的连接");
            } else if (e instanceof ClientAbortException || e.getMessage().contains("SocketTimeoutException")) {
                log.warn("客户端主动关闭连接或网络超时 {}", e.getMessage());
            } else {
                log.error("下载文件过程中出现异常", e);
            }
        }
    }

    /**
     * 移动文件到回收站
     *
     * @param file 文件
     * @return 是否成功
     */
    @SneakyThrows
    public boolean moveToRecycleBin(File file) {
        if (!FileUtils.exists(Setting.USER_RECYCLE_BIN_DIR_PATH)) {
            FileUtils.createHiddenDirectory(Setting.USER_RECYCLE_BIN_DIR_PATH);
        }

        // 移动文件到回收站
        try {
            String fid = RandomUtil.randomString(4);
            FileUtils.copy(file, new File(getRecycleBinFileRealPath(fid, FileUtils.getFileName(file))));

            RecycleBinFile recycleBinFile = new RecycleBinFile(fid, FileUtils.getFileName(file), FileUtils.getAbsolutePath(file), new Date());
            recycleBinFileMapper.insertByDynamicCondition(recycleBinFile);

            FileUtils.delete(file);
            return true;
        } catch (IOException e) {
            log.error("移动文件到回收站失败", e);
            return false;
        }
    }

    public static String getRecycleBinFileRealPath(String fid, String fileName) {
        return Setting.USER_RECYCLE_BIN_DIR_PATH + "/" + fid + "-" + fileName;
    }


    /**
     * 从回收站中彻底删除文件
     *
     * @param fid 文件ID
     */
    public boolean deleteFromRecycleBinByFid(String fid, HttpServletRequest request) {
        // 根据 fid 查询文件信息
        RecycleBinFile recycleBinFile = recycleBinFileMapper.selectByCode(fid);
        if (recycleBinFile == null) {
            return false;
        }

        try {
            // 删除文件
            FileUtils.delete(new File(getRecycleBinFileRealPath(recycleBinFile.getId(), recycleBinFile.getFileName())));
            // 删除数据库记录
            recycleBinFileMapper.deleteByCode(fid);
            // 打印日志
            operationLogService.addLogFromRequest("删除回收站文件 [" + recycleBinFile.getFileName() + "]", OperationLevel.IMPORTANT, request);
            return true;
        } catch (IOException e) {
            log.error("删除文件失败", e);
            return false;
        }
    }

    /**
     * 清空回收站
     */
    public boolean deleteAllFromRecycleBin() {
        // 查询所有文件信息
        List<RecycleBinFile> recycleBinFileList = recycleBinFileMapper.selectAll();
        for (RecycleBinFile recycleBinFile : recycleBinFileList) {
            String recycleBinFileRealPath = getRecycleBinFileRealPath(recycleBinFile.getId(), recycleBinFile.getFileName());
            try {
                // 删除文件
                FileUtils.delete(recycleBinFileRealPath);
            } catch (IOException e) {
                log.error("删除文件 [{}] 失败", recycleBinFileRealPath, e);
                return false;
            }
        }
        // 删除数据库记录
        recycleBinFileMapper.deleteAll();
        return true;
    }


    /**
     * 从回收站中还原文件
     *
     * @param fid 文件ID
     * @return 是否成功
     */
    public boolean restoreFromRecycleBin(String fid, HttpServletRequest request) {
        // 根据 fid 查询文件信息
        RecycleBinFile recycleBinFile = recycleBinFileMapper.selectByCode(fid);
        if (recycleBinFile == null) {
            return false;
        }

        String recycleBinFileRealPath = getRecycleBinFileRealPath(recycleBinFile.getId(), recycleBinFile.getFileName());
        String fileOriginalPath = recycleBinFile.getFileOriginalPath();
        File originFile = new File(fileOriginalPath);
        if (originFile.exists()) {
            log.error("原文件已存在, 不能还原文件 [{}]", recycleBinFileRealPath);
            return false;
        }

        try {
            // 复制文件到原路径
            FileUtils.copy(recycleBinFileRealPath, fileOriginalPath);
            // 删除数据库记录
            recycleBinFileMapper.deleteByCode(fid);
            // 删除回收站文件
            FileUtils.delete(new File(recycleBinFileRealPath));
            // 打印日志
            operationLogService.addLogFromRequest("还原文件 [" + recycleBinFile.getFileName() + "]", OperationLevel.INFO, request);
            return true;
        } catch (IOException e) {
            log.error("还原文件 [{}] 失败", fileOriginalPath, e);
            return false;
        }

    }

    /**
     * 获取回收站列表
     *
     * @return 回收站列表
     */
    @SneakyThrows
    public List<RecycleBinFileVO> getRecycleBinList() {
        final List<RecycleBinFile> recycleBinFileList = recycleBinFileMapper.selectAll();
        ArrayList<RecycleBinFileVO> voList = new ArrayList<>(recycleBinFileList.size());

        for (RecycleBinFile recycleBinFile : recycleBinFileList) {
            LocalDateTime deleteTime = recycleBinFile.getDeleteTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            //计算文件剩余时间
            String remainingTime = getRemainingTime(deleteTime);
            // 懒删除,每次获取的时候判断是否超过 7 天,超过 7 天则执行删除操作(或者每次启动应用时执行删除操作)
            if (remainingTime.equals("-1")) {
                // 执行删除
                FileUtils.delete(new File(getRecycleBinFileRealPath(recycleBinFile.getId(), recycleBinFile.getFileName())));
                // 删除数据库记录
                recycleBinFileMapper.deleteByCode(recycleBinFile.getId());
            } else {
                //返回信息
                RecycleBinFileVO vo = MetaToVoConverter.convert(recycleBinFile, RecycleBinFileVO.class);
                vo.setDeleteTime(EasyTimer.getFormatTime(deleteTime));
                vo.setRemainingTime(remainingTime);
                voList.add(vo);
            }
        }

        return voList;
    }

    //    计算剩余时间
    private static String getRemainingTime(LocalDateTime deleteTime) {
        LocalDateTime now = LocalDateTime.now();

        // 计算时间差
        Duration duration = Duration.between(deleteTime, now);
        long daysDifference = duration.toDays(); // 以天为单位相差多少
        long hoursDifference = duration.toHours(); // 以小时为单位相差多少

        // 判断是否超过 7 天
        if (daysDifference >= 7) {
            // 超过 7 天，执行删除操作
            return "-1";
        } else {


            if (hoursDifference <= 144) {
                // 剩余时间大于等于24小时, 显示剩余天数.
                long remainingDays = 7 - daysDifference;
                return remainingDays + "天";
            } else {
                // 剩余时间小于 24 小时, 显示剩余小时. Ps:  144 = 24 * 6
                long remainingHours = 24 - (hoursDifference % 24);
                return remainingHours + "小时";
            }

        }
    }
}