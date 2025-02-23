package com.muyangyo.filesyncclouddisk.manager.controller;

import com.muyangyo.filesyncclouddisk.common.aspect.annotations.AdminRequired;
import com.muyangyo.filesyncclouddisk.common.aspect.annotations.LocalOperation;
import com.muyangyo.filesyncclouddisk.common.model.dto.CreateSyncDTO;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import com.muyangyo.filesyncclouddisk.manager.service.SyncInfoService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/23
 * Time: 20:18
 */
@RestController
@LocalOperation
@AdminRequired
@RequestMapping("/api/syncManager")
public class SyncManagerController {
    @Resource
    private SyncInfoService syncInfoService;

    @PostMapping("/addSyncInfo")
    public Result addSyncInfo(@RequestBody CreateSyncDTO createSyncDTO) {
        if (StringUtils.hasLength(createSyncDTO.getSyncName()) && StringUtils.hasLength(createSyncDTO.getLocalPath())) {
            String formLocalPath = FileUtils.normalizePath(createSyncDTO.getLocalPath());
            return syncInfoService.addSyncInfo(createSyncDTO.getSyncName(), formLocalPath); // 这个一定是服务端的
        } else if (StringUtils.hasLength(createSyncDTO.getKey())) {
            //todo: 这里建议加密解密下
            return syncInfoService.addSyncInfoByKey(createSyncDTO.getKey()); // 这个一定是客户端的
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
