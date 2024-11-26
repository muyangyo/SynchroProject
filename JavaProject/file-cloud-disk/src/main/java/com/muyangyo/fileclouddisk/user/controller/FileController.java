package com.muyangyo.fileclouddisk.user.controller;

import com.muyangyo.fileclouddisk.common.aspect.HandlerVideoBinConfig;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.enums.FileCategory;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.model.vo.FileInfo;
import com.muyangyo.fileclouddisk.common.model.vo.VideoFileInfo;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.user.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/25
 * Time: 13:25
 */
@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {
    @Resource
    private Setting setting;
    private static final HashMap<String, File> VideoCache = new HashMap<>(10); // 根据IP缓存视频文件
    @Resource
    private FileService fileService;

    @PostMapping(value = "/getFileList") // 不能改为get,因为有路径会导致问题
    public Result getFileList(@RequestBody String path) {
        path = normalizeParams(path); // 格式化参数
        LinkedList<FileInfo> fileInfos = fileService.getFileInfoList(path);
        return Result.success(fileInfos);
    }

    @PostMapping(value = "/previewDocx")
    public void previewDocx(@RequestBody String path, HttpServletResponse response) throws IOException {
        previewFileExceptVideo(path, response);
    }

    @PostMapping(value = "/previewImage")
    public void previewImage(@RequestBody String path, HttpServletResponse response) throws IOException {
        previewFileExceptVideo(path, response);
    }

    @PostMapping("/previewAudio")
    public void previewAudio(@RequestBody String path, HttpServletResponse response) throws IOException {
        previewFileExceptVideo(path, response);
    }

    @PostMapping("/previewPdf")
    public void previewPdf(@RequestBody String path, HttpServletResponse response) throws IOException {
        previewFileExceptVideo(path, response);
    }

    @PostMapping("/previewTxt")
    public void previewTxt(@RequestBody String path, HttpServletResponse response) throws IOException {
        previewFileExceptVideo(path, response);
    }

    @Resource
    HandlerVideoBinConfig handlerVideoBinConfig;

    @PostMapping("/preparingVideo")
    public Result preparingVideo(@RequestBody String path, HttpServletRequest request) throws ServletException, IOException {
        path = normalizeParams(path);
        File file = new File(path);
        FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file); // 检查路径是否合法
        if (fileInfo.getFileType().getCategory() == FileCategory.VIDEO) {
            String ip = NetworkUtils.getClientIp(request);
            VideoCache.remove(ip);// 清除缓存
            VideoCache.put(ip, file);

            VideoFileInfo videoFileInfo = new VideoFileInfo();
            videoFileInfo.setUrl(setting.getCompleteServerURL() + "/file/previewVideo");
            videoFileInfo.setFileName(fileInfo.getFileName());
            videoFileInfo.setFileSize(fileInfo.getFileSize());
            videoFileInfo.setModifiedTime(fileInfo.getModifiedTime());
            videoFileInfo.setFilePath(fileInfo.getFilePath());
            videoFileInfo.setMountRootPath(fileInfo.getMountRootPath());
            videoFileInfo.setFileType(fileInfo.getFileType());
            return Result.success(videoFileInfo);
        }
        return Result.error("无法准备预览该文件");
    }


    @RequestMapping("/previewVideo")
    public void video(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file = VideoCache.get(NetworkUtils.getClientIp(request));
        if (file == null || !file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file); // 检查路径是否合法

        request.setAttribute(HandlerVideoBinConfig.ATTR_FILE, FileUtils.getAbsolutePath(file));
        handlerVideoBinConfig.handleRequest(request, response);
    }

    private void previewFileExceptVideo(String path, HttpServletResponse response) throws IOException {
        path = normalizeParams(path);

        File file = new File(path);
        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file); // 检查路径是否合法

        response.setContentType(fileInfo.getFileType().getMimeType()); // 根据文件类型设置Content-Type
        response.setHeader("Content-Disposition", "inline; filename=" + FileUtils.getFileName(file));

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

    // 格式化参数(去除双引号和格式化路径)
    private String normalizeParams(String path) {
        path = path.replace("\"", "");
        return FileUtils.normalizePath(path);
    }
}