package com.muyangyo.fileclouddisk.manager.controller;

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
public class ShareFolderController {

    @Resource
    private ShareFolderService shareFolderService;

    @PostMapping("/addShareFolder")
    public Result addShareFolder() {
        return shareFolderService.addNewShareFolder();
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
