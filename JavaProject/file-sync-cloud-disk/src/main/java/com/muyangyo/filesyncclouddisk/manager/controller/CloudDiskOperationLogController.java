package com.muyangyo.filesyncclouddisk.manager.controller;

import com.muyangyo.filesyncclouddisk.common.aspect.annotations.AdminRequired;
import com.muyangyo.filesyncclouddisk.common.aspect.annotations.LocalOperation;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.manager.service.CloudDiskOperationLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/18
 * Time: 15:59
 */
@RequestMapping("/api/cloudDiskOperationLog")
@RestController
@LocalOperation
@AdminRequired
public class CloudDiskOperationLogController {
    @Resource
    private CloudDiskOperationLogService cloudDiskOperationLogService;

    @GetMapping("/getLogList")
    public Result getLogList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }

        return Result.success(cloudDiskOperationLogService.getLogList(page - 1, pageSize));// page-1 因为前端传过来的是从1开始的
    }

    @DeleteMapping("/deleteLog")
    public Result deleteLog() {
        cloudDiskOperationLogService.deleteAllLog();
        return Result.success(true);
    }
}
