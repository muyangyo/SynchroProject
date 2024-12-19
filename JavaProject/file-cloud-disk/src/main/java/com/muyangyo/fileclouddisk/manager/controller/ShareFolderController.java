package com.muyangyo.fileclouddisk.manager.controller;

import com.muyangyo.fileclouddisk.common.aspect.annotations.AdminRequired;
import com.muyangyo.fileclouddisk.common.aspect.annotations.LocalOperation;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.dto.FilePathDTO;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.manager.service.ShareFolderService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
@AdminRequired
public class ShareFolderController {

    @Resource
    private ShareFolderService shareFolderService;

    @Resource
    private Setting setting;

    @PostMapping("/addShareFolder")
    public Result addShareFolder(@RequestBody(required = false) FilePathDTO filePathDTO, HttpServletRequest request) {
        if (filePathDTO != null && StringUtils.hasLength(filePathDTO.getPath())) {
            // 远程添加
            String path = FileUtils.normalizePath(filePathDTO.getPath());
            return shareFolderService.addNewShareFolderByPath(path, request);
        } else {
            if (!NetworkUtils.isLocalhost(request)) {
                return Result.fail("不支持远程操作!");
            }
            // 是本地请求则打开选择框
            return shareFolderService.addNewShareFolder(request);
        }
    }

    @GetMapping("/getShareFolderList")
    public Result getShareFolderList(HttpServletRequest request) {
        return shareFolderService.getShareFolderList(request);
    }

    @PostMapping("/openFolder")
    public Result openFolder(@RequestBody FilePathDTO filePathDTO, HttpServletRequest request) {
        if (!NetworkUtils.isLocalhost(request)) {
            return Result.fail("不支持远程操作!");
        }

        String path = filePathDTO.getPath();
        if (StringUtils.hasLength(path)) {
            path = FileUtils.normalizePath(path);
            return shareFolderService.openFolder(path, request);
        }
        return Result.fail("路径不能为空");
    }

    @PostMapping("/deleteShareFolder")
    public Result deleteShareFolder(@RequestBody FilePathDTO filePathDTO, HttpServletRequest request) {
        String path = filePathDTO.getPath();
        if (StringUtils.hasLength(path)) {
            path = FileUtils.normalizePath(path);
            return shareFolderService.deleteShareFolder(path, request);
        }
        return Result.fail("路径不能为空");
    }
}
