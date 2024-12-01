package com.muyangyo.fileclouddisk.user.controller;

import cn.hutool.core.util.RandomUtil;
import com.muyangyo.fileclouddisk.common.aspect.HandlerFileBinConfig;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.dto.FilePathDTO;
import com.muyangyo.fileclouddisk.common.model.dto.RenameFileDTO;
import com.muyangyo.fileclouddisk.common.model.enums.FileCategory;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.model.vo.DownloadFileInfo;
import com.muyangyo.fileclouddisk.common.model.vo.FileInfo;
import com.muyangyo.fileclouddisk.common.model.vo.VideoFileInfo;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import com.muyangyo.fileclouddisk.user.service.FileService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.TimerTask;

@RestController
@Slf4j
@RequestMapping("/file")
public class FileController {
    @Resource
    private FileService fileService;

    @Resource
    private Setting setting;

    @Resource
    HandlerFileBinConfig handlerFileBinConfig;// 让文件支持range请求

    /**
     * 使用伪路径
     *
     * @param filePath 路径
     * @return 文件列表
     */
    @SneakyThrows
    @PostMapping(value = "/getFileList")
    public Result getFileList(@RequestBody FilePathDTO filePath) {
        String path = URLDecoder.decode(filePath.getPath(), StandardCharsets.UTF_8.toString());// 解码路径(防止中文乱码)
        path = FileUtils.normalizePath(path); // 规范路径
        LinkedList<FileInfo> fileInfos = fileService.getFileInfoList(path); // 获取文件列表
        return Result.success(fileInfos);
    }

    @PostMapping(value = "/previewDocx")
    public void previewDocx(@RequestBody FilePathDTO filePath, HttpServletResponse response) throws IOException {
        fileService.previewFile(filePath.getPath(), response);
    }

    @PostMapping(value = "/previewImage")
    public void previewImage(@RequestBody FilePathDTO filePath, HttpServletResponse response) throws IOException {
        fileService.previewFile(filePath.getPath(), response);
    }

    @PostMapping("/previewAudio")
    public void previewAudio(@RequestBody FilePathDTO filePath, HttpServletResponse response) throws IOException {
        fileService.previewFile(filePath.getPath(), response);
    }

    @PostMapping("/getPreviewAudioInfo")
    public Result getPreviewAudioName(@RequestBody FilePathDTO filePath) {
        String path = FileUtils.normalizePath(filePath.getPath());
        File file = new File(path);
        FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file);
        return Result.success(fileInfo);
    }

    @PostMapping("/previewPdf")
    public void previewPdf(@RequestBody FilePathDTO filePath, HttpServletResponse response) throws IOException {
        fileService.previewFile(filePath.getPath(), response);
    }

    @PostMapping("/previewTxt")
    public void previewTxt(@RequestBody FilePathDTO filePath, HttpServletResponse response) throws IOException {
        fileService.previewFile(filePath.getPath(), response);
    }

    @PostMapping("/preparingVideo")
    public Result preparingVideo(@RequestBody FilePathDTO filePath) {
        String path = FileUtils.normalizePath(filePath.getPath());
        File file = new File(path);
        FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file);
        if (fileInfo.getFileType().getCategory() == FileCategory.VIDEO) {
            String vid = RandomUtil.randomString(8);
            setting.getVideoCache().put(vid, file);

            VideoFileInfo videoFileInfo = new VideoFileInfo();
            videoFileInfo.setUrl("/api/file/previewVideo?vid=" + vid);// 只能返回相对地址
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

    @GetMapping(value = "/previewVideo")
    public void video(String vid, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        File file = setting.getVideoCache().get(vid);// 获取缓存的视频文件
        try {
            if (file == null || !file.exists()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            request.setAttribute(HandlerFileBinConfig.ATTR_FILE, FileUtils.getAbsolutePath(file));
            handlerFileBinConfig.handleRequest(request, response);
        } catch (IOException e) {
            if (e.getMessage().contains("远程主机强迫关闭了一个现有的连接")) {
                log.info("由于视频拖动导致远程主机强迫关闭了一个现有的连接");
            } else {
                e.printStackTrace();
            }
        }
    }


    @SneakyThrows
    @PostMapping(value = "/preparingDownloadFile")
    public Result preparingDownloadFile(@RequestBody FilePathDTO filePath) {
        String path = FileUtils.normalizePath(filePath.getPath());
        File file = new File(path);
        FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file);
        if (fileInfo.getFileType().getCategory() != FileCategory.FOLDER) {
            String fid = RandomUtil.randomString(5);
            setting.getFileCache().put(fid, file);
            return Result.success(new DownloadFileInfo(fileInfo.getFileName(), "/file/download?fid=" + fid));// 只能返回相对地址
        } else {

            FileUtils.createHiddenDirectory(Setting.USER_DOWNLOAD_TEMP_DIR_PATH);
            File tempDestZip = new File(Setting.USER_DOWNLOAD_TEMP_DIR_PATH, RandomUtil.randomString(5) + ".zip");

            log.info("开始压缩文件夹：" + filePath.getPath());
            FileUtils.zip(filePath.getPath(), tempDestZip);
            log.info("压缩文件成功：" + FileUtils.getAbsolutePath(tempDestZip));
            setting.getFileCache().put(FileUtils.getFileNameWithoutExtension(tempDestZip), tempDestZip);


            // 定时删除压缩文件
            setting.getCustomTimer().scheduleTask(new TimerTask() {
                @Override
                public void run() {
                    tempDestZip.delete(); // 定时删除压缩文件
                }
            }, (long) setting.getFileCacheExpireTime() * 60); // 定时删除压缩文件


            return Result.success(new DownloadFileInfo(FileUtils.getFileName(tempDestZip), "/file/download?fid=" + FileUtils.getFileNameWithoutExtension(tempDestZip)));// 只能返回相对地址
        }
    }

    @SneakyThrows
    @GetMapping(value = "/download")
    public void downloadFile(@RequestParam String fid, HttpServletResponse response, HttpServletRequest request) {
        File file = setting.getFileCache().get(fid);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 1.如果压缩文件在临时文件夹中通过,如果没有则再判断是否存在于挂载目录中
        boolean isTempZip = false;
        if (FileUtils.getFileExtension(file).equals("zip")) {
            if (new File(Setting.USER_DOWNLOAD_TEMP_DIR_PATH, FileUtils.getFileName(file)).exists()) {
                isTempZip = true;
            }
        }

        // 2. 如果是其他的文件,或者原本就在挂载目录下的压缩文件,则需要再判断一次
        if (!isTempZip) {
            FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file);
        }

        request.setAttribute(HandlerFileBinConfig.ATTR_FILE, FileUtils.getAbsolutePath(file));
        handlerFileBinConfig.handleRequest(request, response);
    }


    /**
     * 重命名文件
     *
     * @param renameFileDTO 文件路径和新名称
     * @return 重命名结果
     */
    @SneakyThrows
    @PostMapping("/renameFile")
    public Result renameFile(@RequestBody RenameFileDTO renameFileDTO) {
        String oldPath = FileUtils.normalizePath(renameFileDTO.getOldPath());

        if (fileService.isRootPath(oldPath)) {
            return Result.error("根目录不能重命名");
        }

        File oldFile = new File(oldPath);
        if (!oldFile.exists()) {
            return Result.error("文件不存在");
        }

        fileService.checkFollowRootPathAndGetFileInfo(new File(oldPath));// 检查文件是否合理


        String newName = renameFileDTO.getNewName();// 新名称


        return Result.success(FileUtils.rename(oldPath, newName));
    }

    /**
     * 删除文件
     *
     * @param filePathDTO 文件路径
     * @return 删除结果
     */
    @SneakyThrows
    @PostMapping("/deleteFile")
    public Result deleteFile(@RequestBody FilePathDTO filePathDTO) {
        String path = FileUtils.normalizePath(filePathDTO.getPath());

        if (fileService.isRootPath(path)) {
            return Result.error("根目录不能删除");
        }

        File file = new File(path);

        if (!file.exists()) {
            return Result.error("文件不存在");
        }

        fileService.checkFollowRootPathAndGetFileInfo(file);// 检查文件是否合理


        if (FileUtils.delete(file)) {
            return Result.success("文件删除成功");
        } else {
            return Result.error("文件删除失败");
        }
    }

    @PostMapping("/shareFile") //todo 等待实现文件分享功能
    public Result shareFile(@RequestBody FilePathDTO filePathDTO) {
        String path = FileUtils.normalizePath(filePathDTO.getPath());
        File file = new File(path);
        fileService.checkFollowRootPathAndGetFileInfo(file);// 检查文件是否合理

        String fid = RandomUtil.randomString(5);

        return Result.success(fid);
    }



}