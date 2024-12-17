package com.muyangyo.fileclouddisk.manager.controller;

import com.muyangyo.fileclouddisk.common.aspect.annotations.LocalOperation;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.dto.FilePathDTO;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import com.muyangyo.fileclouddisk.manager.service.ShareFolderService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/16
 * Time: 19:59
 */
@RestController
@RequestMapping("/shareFolderManager")
@LocalOperation
public class ShareFolderController {

    @Resource
    private ShareFolderService shareFolderService;

    @Resource
    private Setting setting;

    @PostMapping("/addShareFolder")
    public Result addShareFolder(@RequestBody(required = false) FilePathDTO filePathDTO) {
        if (filePathDTO != null && StringUtils.hasLength(filePathDTO.getPath())) { // 如果是通过路径来增加的话,有两种情况: 1.系统开启了远程添加 2.系统关闭了远程添加
            if (setting.isLocalOperationOnly()) { // 如果有限制只能本地操作
                return Result.fail("不支持远程操作!");
            }
            // 远程添加
            String path = FileUtils.normalizePath(filePathDTO.getPath());
            return shareFolderService.addNewShareFolderByPath(path);
        } else {
            // 是本地请求则直接打开选择框
            return shareFolderService.addNewShareFolder();
        }
    }

    @GetMapping("/getShareFolderList")
    public Result getShareFolderList() {
        return shareFolderService.getShareFolderList();
    }

    @PostMapping("/openFolder")
    public Result openFolder(@RequestBody FilePathDTO filePathDTO) {
        String path = filePathDTO.getPath();
        if (StringUtils.hasLength(path)) {
            path = FileUtils.normalizePath(path);
            return shareFolderService.openFolder(path);
        }
        return Result.fail("路径不能为空");
    }

    @PostMapping("/deleteShareFolder")
    public Result deleteShareFolder(@RequestBody FilePathDTO filePathDTO) {
        String path = filePathDTO.getPath();
        if (StringUtils.hasLength(path)) {
            path = FileUtils.normalizePath(path);
            return shareFolderService.deleteShareFolder(path);
        }
        return Result.fail("路径不能为空");
    }
}
