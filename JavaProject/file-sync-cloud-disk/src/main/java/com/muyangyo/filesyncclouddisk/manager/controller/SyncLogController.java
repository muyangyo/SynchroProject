package com.muyangyo.filesyncclouddisk.manager.controller;

import com.muyangyo.filesyncclouddisk.common.aspect.annotations.AdminRequired;
import com.muyangyo.filesyncclouddisk.common.aspect.annotations.LocalOperation;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.manager.service.SyncLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/24
 * Time: 17:33
 */
@RequestMapping("/api/syncLog")
@RestController
@LocalOperation
@AdminRequired
public class SyncLogController {
    @Resource
    private SyncLogService syncLogService;

    @GetMapping("/getLogList")
    public Result getLogList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }

        return Result.success(syncLogService.getLogList(page - 1, pageSize));// page-1 因为前端传过来的是从1开始的
    }

    @DeleteMapping("/deleteLog")
    public Result deleteLog() {
        syncLogService.deleteAllLog();
        return Result.success(true);
    }

}
