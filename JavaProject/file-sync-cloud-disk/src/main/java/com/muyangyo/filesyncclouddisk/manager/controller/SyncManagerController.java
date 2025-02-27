package com.muyangyo.filesyncclouddisk.manager.controller;

import com.muyangyo.filesyncclouddisk.common.aspect.annotations.AdminRequired;
import com.muyangyo.filesyncclouddisk.common.aspect.annotations.LocalOperation;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.model.dto.CreateSyncLinkDTO;
import com.muyangyo.filesyncclouddisk.common.model.dto.SyncDTO;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import com.muyangyo.filesyncclouddisk.common.utils.NetworkUtils;
import com.muyangyo.filesyncclouddisk.manager.service.SyncInfoService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/23
 * Time: 20:18
 * Desc: 这个服务端和客户端都兼容
 */
@RestController
@LocalOperation
@AdminRequired
@RequestMapping("/api/syncManager")
public class SyncManagerController {
    @Resource
    private SyncInfoService syncInfoService;
    @Resource
    private Setting setting;

    @DeleteMapping("/deleteSyncInfo")
    public Result deleteSyncInfo(String syncName) {
        if (StringUtils.hasLength(syncName)) {
            return syncInfoService.deleteSyncInfo(syncName);
        }
        return Result.fail("参数错误!");
    }

    @PostMapping("/updateSyncInfo")
    public Result updateSyncInfo(@RequestBody SyncDTO syncDTO) {
        if (StringUtils.hasLength(syncDTO.getLocalPath()) && StringUtils.hasLength(syncDTO.getSyncName())) {
            syncDTO.setLocalPath(FileUtils.normalizePath(syncDTO.getLocalPath()));
            if (setting.isSyncServer()) {
                return syncInfoService.updateSyncInfoOfServer(syncDTO.getSyncName(), syncDTO.getLocalPath());
            } else {
                return syncInfoService.updateSyncInfoOfClient(syncDTO.getSyncName(), syncDTO.getLocalPath(), syncDTO.getServerIp());
            }

        } else if (StringUtils.hasLength(syncDTO.getNewStatus()) && StringUtils.hasLength(syncDTO.getSyncName())) {
            return syncInfoService.pauseOrStartSync(syncDTO.getNewStatus(), syncDTO.getSyncName());
        }
        return Result.fail("参数错误!");
    }

    @GetMapping("/getSyncStatusClientOnly")
    public Result getSyncStatusClientOnly(@RequestParam(required = false) List<String> syncNames) {
        if (syncNames != null && !syncNames.isEmpty()) {
            return Result.success(syncInfoService.getSyncStatusClientOnly(syncNames));
        }
        return Result.fail("参数错误!");
    }

    @PostMapping("/getSyncShareLink")
    public Result getSyncShareLink(@RequestBody CreateSyncLinkDTO createSyncLinkDTO) {
        if (StringUtils.hasLength(createSyncLinkDTO.getSyncName())) {
            return syncInfoService.getSyncShareLink(createSyncLinkDTO.getSyncName(), createSyncLinkDTO.getKey());
        }
        return Result.fail("参数错误!");
    }

    @GetMapping("/getPublicKey")
    public Result getPublicKey(HttpServletRequest request) {
        return syncInfoService.getPublicKey(NetworkUtils.getClientIp(request));
    }

    @PostMapping("/addSyncInfo")
    public Result addSyncInfo(@RequestBody SyncDTO syncDTO, HttpServletRequest request) {
        if (StringUtils.hasLength(syncDTO.getSyncName()) && StringUtils.hasLength(syncDTO.getLocalPath())) {
            String formLocalPath = FileUtils.normalizePath(syncDTO.getLocalPath());
            return syncInfoService.addSyncInfo(syncDTO.getSyncName(), formLocalPath); // 这个一定是服务端的
        } else if (StringUtils.hasLength(syncDTO.getKey()) && StringUtils.hasLength(syncDTO.getLocalPath())) {
            syncInfoService.decryptSyncDTO(syncDTO, NetworkUtils.getClientIp(request));
            String formLocalPath = FileUtils.normalizePath(syncDTO.getLocalPath());
            return syncInfoService.addSyncInfoByKey(syncDTO.getKey(), formLocalPath); // 这个一定是客户端的
        } else {
            return Result.fail("参数错误");
        }
    }

    @RequestMapping("/changeStatus")
    public Result changeStatus(String changeTo) {
        if (StringUtils.hasLength(changeTo)) {
            return syncInfoService.changeSyncStatus(changeTo);
        }
        return Result.fail("参数错误");
    }

    @GetMapping("/getStatus")
    public Result getStatus() {
        return syncInfoService.getSyncStatus();
    }

    @GetMapping("/getSyncInfoList")
    public Result getSyncInfoList() {
        return syncInfoService.getSyncInfoList();
    }
}
