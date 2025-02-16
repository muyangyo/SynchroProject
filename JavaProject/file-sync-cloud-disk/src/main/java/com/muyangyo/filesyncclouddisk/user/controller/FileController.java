package com.muyangyo.filesyncclouddisk.user.controller;

import cn.hutool.core.util.RandomUtil;
import com.muyangyo.filesyncclouddisk.common.aspect.FileBinHandler;
import com.muyangyo.filesyncclouddisk.common.aspect.annotations.UserOperationLimit;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.model.dto.CreateFolderDTO;
import com.muyangyo.filesyncclouddisk.common.model.dto.FilePathDTO;
import com.muyangyo.filesyncclouddisk.common.model.dto.RenameFileDTO;
import com.muyangyo.filesyncclouddisk.common.model.enums.FileCategory;
import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.filesyncclouddisk.common.model.meta.ShareFile;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.model.vo.DownloadFileInfo;
import com.muyangyo.filesyncclouddisk.common.model.vo.FileInfo;
import com.muyangyo.filesyncclouddisk.common.model.vo.VideoFileInfo;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import com.muyangyo.filesyncclouddisk.common.utils.NetworkUtils;
import com.muyangyo.filesyncclouddisk.common.utils.TokenUtils;
import com.muyangyo.filesyncclouddisk.manager.service.OperationLogService;
import com.muyangyo.filesyncclouddisk.user.service.FileService;
import com.muyangyo.filesyncclouddisk.user.service.ShareFileService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimerTask;

@RestController
@Slf4j
@RequestMapping("/api/file")
public class FileController {
    @Resource
    private FileService fileService;

    @Resource
    private ShareFileService shareFileService;

    @Resource
    private Setting setting;

    @Resource
    FileBinHandler fileBinHandler;// 让文件支持range请求

    @Resource
    private OperationLogService operationLogService;

    /**
     * 使用伪路径
     *
     * @param filePath 路径
     * @return 文件列表
     */
    @SneakyThrows
    @UserOperationLimit("r")
    @PostMapping(value = "/getFileList")
    public Result getFileList(@RequestBody FilePathDTO filePath, HttpServletRequest request) {
        String path = URLDecoder.decode(filePath.getPath(), StandardCharsets.UTF_8.toString());// 解码路径(防止中文乱码)
        path = FileUtils.normalizePath(path); // 规范路径
        LinkedList<FileInfo> fileInfos = fileService.getFileInfoList(path, request); // 获取文件列表
        return Result.success(fileInfos);
    }

    /**
     * 创建文件夹(使用伪路径)
     *
     * @param createFolderDTO 创建文件夹参数
     * @return 创建结果
     */
    @PostMapping("/createFolder")
    @UserOperationLimit("w")
    public Result createFolder(@RequestBody CreateFolderDTO createFolderDTO, HttpServletRequest request) {
        String parentPath = FileUtils.normalizePath(createFolderDTO.getParentPath());


        if (StringUtils.hasLength(parentPath)) {
            return fileService.createFolder(parentPath, createFolderDTO.getFolderName(), request);
        } else {
            return Result.fail("文件夹路径不能为空");
        }
    }

    @PostMapping("/uploadChunk")
    @UserOperationLimit("w")
    public Result uploadChunk(
            @RequestParam("parentPath") String parentPath,// 父路径
            @RequestParam("file") MultipartFile file, // 上传的文件
            @RequestParam("chunkIndex") int chunkIndex, // 当前块的索引
            @RequestParam("fileName") String fileName, // 文件名
            @RequestParam("totalChunks") int totalChunks, // 当前文件总块数
            @RequestParam("chunkMd5") String chunkMd5, // 分块文件的MD5用于验证完整性
            HttpServletRequest request
    ) {
        fileService.uploadChunk(parentPath, file, chunkIndex, fileName, totalChunks, chunkMd5, request);
        return Result.success("上传成功!");
    }

    @PostMapping(value = "/previewDocx")
    @UserOperationLimit("r")
    public void previewDocx(@RequestBody FilePathDTO filePath, HttpServletResponse response, HttpServletRequest request) throws IOException {
        fileService.previewFile(filePath.getPath(), response, request);
    }

    @PostMapping(value = "/previewImage")
    @UserOperationLimit("r")
    public void previewImage(@RequestBody FilePathDTO filePath, HttpServletResponse response, HttpServletRequest request) throws IOException {
        fileService.previewFile(filePath.getPath(), response, request);
    }

    @UserOperationLimit("r")
    @PostMapping("/previewAudio")
    public void previewAudio(@RequestBody FilePathDTO filePath, HttpServletResponse response, HttpServletRequest request) throws IOException {
        fileService.previewFile(filePath.getPath(), response, request);
    }

    @UserOperationLimit("r")
    @PostMapping("/getPreviewAudioInfo")
    public Result getPreviewAudioName(@RequestBody FilePathDTO filePath, HttpServletRequest request) {
        String path = FileUtils.normalizePath(filePath.getPath());
        File file = new File(path);
        FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file);
        operationLogService.addLogFromRequest("预览文件", OperationLevel.INFO, request);
        return Result.success(fileInfo);
    }

    @UserOperationLimit("r")
    @PostMapping("/previewPdf")
    public void previewPdf(@RequestBody FilePathDTO filePath, HttpServletResponse response, HttpServletRequest request) throws IOException {
        fileService.previewFile(filePath.getPath(), response, request);
    }

    @UserOperationLimit("r")
    @PostMapping("/previewTxt")
    public void previewTxt(@RequestBody FilePathDTO filePath, HttpServletResponse response, HttpServletRequest request) throws IOException {
        fileService.previewFile(filePath.getPath(), response, request);
    }

    @UserOperationLimit("r")
    @PostMapping("/preparingVideo")
    public Result preparingVideo(@RequestBody FilePathDTO filePath, HttpServletRequest request) {
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

            operationLogService.addLogFromRequest("预览文件", OperationLevel.INFO, request);

            return Result.success(videoFileInfo);
        }
        return Result.error("无法准备预览该文件");
    }

    @UserOperationLimit("r")
    @GetMapping(value = "/previewVideo")
    public void video(String vid, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        File file = setting.getVideoCache().get(vid);// 获取缓存的视频文件
        try {
            if (file == null || !file.exists()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            operationLogService.addLogFromRequest("预览文件", OperationLevel.INFO, request);
            request.setAttribute(FileBinHandler.ATTR_FILE, FileUtils.getAbsolutePath(file));
            fileBinHandler.handleRequest(request, response);
        } catch (IOException e) {
            if (e.getMessage().contains("远程主机强迫关闭了一个现有的连接") || e.getMessage().contains("你的主机中的软件中止了一个已建立的连接") || e.getMessage().contains("Broken pipe")) {
                log.warn("由于视频拖动导致客户端强迫关闭了一个现有的连接 {}", e.getMessage());
            } else {
                log.error("预览视频过程中出现异常", e);
            }
        }
    }


    @UserOperationLimit("r")
    @SneakyThrows
    @PostMapping(value = "/preparingDownloadFile")
    public Result preparingDownloadFile(@RequestBody FilePathDTO filePath, HttpServletRequest request) {
        String path = FileUtils.normalizePath(filePath.getPath());
        File file = new File(path);
        FileInfo fileInfo = fileService.checkFollowRootPathAndGetFileInfo(file);
        operationLogService.addLogFromRequest("准备下载文件", OperationLevel.INFO, request);
        if (fileInfo.getFileType().getCategory() != FileCategory.FOLDER) {
            String fid = RandomUtil.randomString(5);
            setting.getFileCache().put(fid, file);
            return Result.success(new DownloadFileInfo(fileInfo.getFileName(), "/file/download?fid=" + fid, fileInfo.getFileSize()));// 只能返回相对地址
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
            }, (long) setting.getDownloadFileCacheExpireTime() * 60); // 定时删除压缩文件

            return Result.success(
                    new DownloadFileInfo(FileUtils.getFileName(tempDestZip),
                            "/file/download?fid=" + FileUtils.getFileNameWithoutExtension(tempDestZip), tempDestZip.length())
            );// 只能返回相对地址
        }
    }

/*    @UserOperationLimit("r")
    @SneakyThrows
    @GetMapping(value = "/download/{fid}") // 下载文件老版本(@deprecated)
    public void downloadFile(@PathVariable String fid, HttpServletResponse response, HttpServletRequest request) {
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
        operationLogService.addLogFromRequest("下载文件", OperationLevel.INFO, request);

        request.setAttribute(FileBinHandler.ATTR_FILE, FileUtils.getAbsolutePath(file));
        fileBinHandler.handleRequest(request, response);
    }*/

    @UserOperationLimit("r")
    @SneakyThrows
    @GetMapping(value = "/download")
    public void downloadFile(@RequestParam String fid, HttpServletRequest request, HttpServletResponse response) {
        File file = setting.getFileCache().get(fid);

        // 校验文件是否合法
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
        operationLogService.addLogFromRequest("下载文件", OperationLevel.INFO, request);

        // 下载文件
        fileService.downloadFile(file, request, response);
    }


    /**
     * 重命名文件
     *
     * @param renameFileDTO 文件路径和新名称
     * @return 重命名结果
     */
    @UserOperationLimit("w")
    @SneakyThrows
    @PostMapping("/renameFile")
    public Result renameFile(@RequestBody RenameFileDTO renameFileDTO, HttpServletRequest request) {
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

        operationLogService.addLogFromRequest("重命名文件", OperationLevel.WARNING, request);

        return Result.success(FileUtils.rename(oldPath, newName));
    }

    /**
     * 删除文件
     *
     * @param filePathDTO 文件路径
     * @return 删除结果
     */
    @SneakyThrows
    @UserOperationLimit("d")
    @PostMapping("/deleteFile")
    public Result deleteFile(@RequestBody FilePathDTO filePathDTO, HttpServletRequest request) {
        String path = FileUtils.normalizePath(filePathDTO.getPath());

        if (fileService.isRootPath(path)) {
            return Result.error("根目录不能删除");
        }

        File file = new File(path);

        if (!file.exists()) {
            return Result.error("文件不存在");
        }

        fileService.checkFollowRootPathAndGetFileInfo(file);// 检查文件是否合理

        // 判断是否开启回收站功能
        if (setting.isUseRecycleBin()) {
            // 移动到回收站
            if (fileService.moveToRecycleBin(file)) {
                operationLogService.addLogFromRequest("移动文件 [" + FileUtils.getFileName(file) + "] 至回收站", OperationLevel.WARNING, request);
                return Result.success("文件已移至回收站");
            } else {
                return Result.error("文件移至回收站失败");
            }
        } else {
            // 直接删除文件
            if (FileUtils.delete(file)) {
                operationLogService.addLogFromRequest("删除文件 [" + FileUtils.getFileName(file) + "]", OperationLevel.IMPORTANT, request);
                return Result.success("文件删除成功");
            } else {
                return Result.error("文件删除失败");
            }
        }
    }

    @UserOperationLimit("r")
    @GetMapping("/getRecycleBinList")
    public Result getRecycleBinList(HttpServletRequest request) {
        if (setting.isUseRecycleBin()) {
            // 开启回收站功能了才有返回
            operationLogService.addLogFromRequest("获取回收站列表", OperationLevel.INFO, request);
            return Result.success(fileService.getRecycleBinList());
        } else {
            return Result.fail("未开启回收站功能");
        }
    }

    @UserOperationLimit("d")
    @DeleteMapping("/deleteRecycleBinFile")
    public Result deleteRecycleBin(@RequestParam(required = false) String id, @RequestParam boolean isDeleteAll, HttpServletRequest request) {
        if (setting.isUseRecycleBin()) {
            if (isDeleteAll) {
                // 全部删除
                operationLogService.addLogFromRequest("清空回收站", OperationLevel.IMPORTANT, request);
                return fileService.deleteAllFromRecycleBin() ? Result.success("全部删除成功") : Result.error("全部删除失败");
            } else {
                // 单个删除
                return fileService.deleteFromRecycleBinByFid(id, request) ? Result.success("删除成功") : Result.error("删除失败");
            }
        } else {
            return Result.fail("未开启回收站功能");
        }
    }

    @UserOperationLimit("w")
    @PutMapping("/restoreRecycleBinFile")
    public Result restoreRecycleBin(@RequestParam String id, HttpServletRequest request) {
        if (setting.isUseRecycleBin()) {
            return fileService.restoreFromRecycleBin(id, request) ? Result.success("还原成功") : Result.error("还原失败");
        } else {
            return Result.fail("未开启回收站功能");
        }
    }


    @UserOperationLimit("w")
    @PostMapping("/createShareFile") // 创建分享文件(使用的绝对路径)
    public Result createShareFile(@RequestBody FilePathDTO filePathDTO, HttpServletRequest request) {
        String path = FileUtils.normalizePath(filePathDTO.getPath());
        File file = new File(path);
        fileService.checkFollowRootPathAndGetFileInfo(file);// 检查文件是否合理

        //从token中获取用户名
        String token = TokenUtils.getTokenFromCookie(request);
        Map<String, String> tokenLoad = TokenUtils.getTokenLoad(token);
        String username = TokenUtils.TokenLoad.fromMap(tokenLoad).getUsername();


        if (file.exists()) {
            String shareCode = shareFileService.createShareFile(filePathDTO.getPath(), username);
            operationLogService.addLogFromRequest("创建分享文件", OperationLevel.WARNING, request);
            return Result.success("shareCode=" + shareCode);
        }
        return Result.fail("文件不存在");
    }

    @UserOperationLimit("d")
    @DeleteMapping("/deleteShareFile")
    public Result deleteShareFile(@RequestParam String shareCode, HttpServletRequest request) {

        //从token中获取用户名
        String token = TokenUtils.getTokenFromCookie(request);
        Map<String, String> tokenLoad = TokenUtils.getTokenLoad(token);
        String username = TokenUtils.TokenLoad.fromMap(tokenLoad).getUsername();

        shareFileService.deleteShareFileByCode(shareCode, username);
        operationLogService.addLogFromRequest("删除分享链接", OperationLevel.IMPORTANT, request);
        return Result.success("删除成功");
    }

    @UserOperationLimit("d")
    @DeleteMapping("/batchDeleteShareFile")
    public Result batchDeleteShareFile(@RequestParam Boolean isDeleteAll, HttpServletRequest request) {

        //从token中获取用户名
        String token = TokenUtils.getTokenFromCookie(request);
        Map<String, String> tokenLoad = TokenUtils.getTokenLoad(token);
        String username = TokenUtils.TokenLoad.fromMap(tokenLoad).getUsername();

        shareFileService.batchDeleteShareFileByUsername(isDeleteAll, username);
        operationLogService.addLogFromRequest("批量删除分享链接", OperationLevel.IMPORTANT, request);
        return Result.success("删除成功");
    }

    @GetMapping("/getShareFileList")
    @UserOperationLimit("r")
    public Result getShareFileList(HttpServletRequest request, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }

        //从token中获取用户名
        String token = TokenUtils.getTokenFromCookie(request);
        Map<String, String> mapFromToken = TokenUtils.getTokenLoad(token);
        String username = TokenUtils.TokenLoad.fromMap(mapFromToken).getUsername();

        operationLogService.addLogFromRequest("获取分享文件列表", OperationLevel.INFO, request);
        return Result.success(shareFileService.getShareFileListByUsername(username, page - 1, pageSize));// page-1 因为前端传过来的是从1开始的
    }


    @GetMapping("/getShareFile") // 访客模式下获取分享文件(1天有效期)
    public Result getShareFile(@RequestParam String shareCode, @RequestParam(required = false) String parentPath, HttpServletRequest request) throws UnsupportedEncodingException {
        if (shareCode == null || shareCode.length() != 10) {
            return Result.fail("分享码不正确!");
        }

        ShareFile shareFile = shareFileService.getSingleShareFileByCode(shareCode);
        if (!StringUtils.hasLength(parentPath)) {
            if (shareFile == null || shareFile.getStatus() == 0) {
                return Result.fail("分享文件已过期!");
            } else {
                return Result.success(fileService.OutSideGetFileInfoList(shareFile.getFilePath(), request));
            }
        } else {
            String relativePath = URLDecoder.decode(parentPath, StandardCharsets.UTF_8.toString());// 解码路径(防止中文乱码)
            relativePath = FileUtils.normalizePath(relativePath); // 规范路径
            String absolutePath = Setting.USER_SHARE_TEMP_DIR_PATH + "/" + shareCode + "/" + relativePath;
            try {
                return Result.success(fileService.OutSideGetFileInfoList(absolutePath, request));
            } catch (IllegalArgumentException e) {
                return Result.fail("分享文件已过期!");
            }
        }
    }

    @GetMapping("/preparingDownloadShareFile") // 访客模式下准备下载文件
    public Result OutsideFileDownload(@RequestParam String shareCode, @RequestParam(required = false) String parentPath
            , @RequestParam String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 校验文件是否合法
        if (StringUtils.hasLength(parentPath)) {
            parentPath = FileUtils.normalizePath(parentPath);// 规范路径
            parentPath = URLDecoder.decode(parentPath, StandardCharsets.UTF_8.toString());// 解码路径(防止中文乱码)
        }
        String realFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());// 解码文件名(防止中文乱码)

        String realFilePath = Setting.USER_SHARE_TEMP_DIR_PATH + "/" + shareCode + "/" + (StringUtils.hasLength(parentPath) ? (parentPath + "/") : "") + realFileName;

        File file = new File(realFilePath);

        if (!file.exists()) {
            return Result.error("文件不存在");
        }

        operationLogService.addLog("外部用户准备下载文件[ " + file.getName() + " ]", "unknown", NetworkUtils.getClientIp(request), OperationLevel.INFO);
        if (!file.isDirectory()) {
            String fid = RandomUtil.randomString(5);
            setting.getShareFileCache().put(fid, file);
            return Result.success(new DownloadFileInfo(file.getName(), "/file/downloadShareFile?fid=" + fid, file.length()));// 只能返回相对地址
        } else {
            try {
                // 如果是文件夹,则压缩为zip文件
                FileUtils.createHiddenDirectory(Setting.USER_DOWNLOAD_TEMP_DIR_PATH);
                File tempDestZip = new File(Setting.USER_DOWNLOAD_TEMP_DIR_PATH, RandomUtil.randomString(5) + ".zip");

                log.info("开始压缩文件夹：" + realFilePath);
                FileUtils.zip(realFilePath, tempDestZip);
                log.info("压缩文件成功：" + FileUtils.getAbsolutePath(tempDestZip));

                // 定时删除压缩文件
                setting.getCustomTimer().scheduleTask(new TimerTask() {
                    @Override
                    public void run() {
                        tempDestZip.delete(); // 定时删除压缩文件
                    }
                }, setting.getDownloadFileCacheExpireTime() * 60L); // 定时删除压缩文件

                file = tempDestZip;
                setting.getShareFileCache().put(FileUtils.getFileNameWithoutExtension(file), file);
                return Result.success(new DownloadFileInfo(FileUtils.getFileName(file), "/file/downloadShareFile?fid=" + FileUtils.getFileNameWithoutExtension(file), file.length()));// 只能返回相对地址

            } catch (IOException e) {
                log.error("压缩文件失败", e);
                return Result.fail("压缩文件失败");
            }
        }
    }

    @GetMapping(value = "/downloadShareFile")
    public void downloadShareFile(@RequestParam String fid, HttpServletRequest request, HttpServletResponse response) {
        File file = setting.getShareFileCache().get(fid);

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        operationLogService.addLog("外部用户下载文件", "unknown", NetworkUtils.getClientIp(request), OperationLevel.INFO);

        // 下载文件
        fileService.downloadFile(file, request, response);
    }


}
