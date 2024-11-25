package com.muyangyo.fileclouddisk.user.controller;

import com.muyangyo.fileclouddisk.common.exception.IllegalPath;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.model.vo.FileInfo;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import com.muyangyo.fileclouddisk.user.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
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
    private FileService fileService;

    @RequestMapping(value = "/getFileList", method = {RequestMethod.POST})
    public Result getPathFile(@RequestBody() String path) throws IOException {
        // 去除双引号和格式化路径
        path = path.replace("\"", "");
        path = FileUtils.normalizePath(path);

        LinkedList<FileInfo> fileInfos;
        // 根目录
        if (path.equals("root") || path.equals("")) {
            fileInfos = fileService.getRootFileInfo();
            return Result.success(fileInfos);
        }
        // 非根目录
        fileInfos = fileService.getNormalFileInfo(path);
        if (fileInfos == null) {
            throw new IllegalPath("非法路径!该路径不存在于挂载目录中!");
        }
        return Result.success(fileInfos);
    }

//    @Resource
//    private MountDirComponent mountDirService;
//
//    @RequestMapping("/test")
//    public String test() {
//        String path1 = "C:/Users/DR/Desktop/tmp1";
//        String path2 = "C:/Users/DR/Desktop/tmp2";
//
//        //一个的情况
//        LinkedList<String> mountDirList = new LinkedList<>();
//        mountDirList.add(path1);
//        mountDirService.insertMountDir(mountDirList);
//        LinkedList<String> rootMountDir = mountDirService.getRootMountDir();
//        if (rootMountDir.size() != 1) {
//            log.error("rootMountDir size is not 1");
//        }
//        if (!rootMountDir.get(0).equals(path1)) {
//            log.error("rootMountDir is not " + path1);
//        }
//
//        //两个的情况
//        mountDirList.add(path2);
//        mountDirService.insertMountDir(mountDirList);
//        rootMountDir = mountDirService.getRootMountDir();
//        if (rootMountDir.size() != 2)
//            log.error("rootMountDir size is not 2");
//        if (!rootMountDir.get(0).equals(path1) || !rootMountDir.get(1).equals(path2))
//            log.error("rootMountDir is not " + path1 + " or " + path2);
//
//        return "success";
//    }
}
