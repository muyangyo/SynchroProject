package com.muyangyo.fileclouddisk.manager.controller;

import com.muyangyo.fileclouddisk.common.aspect.annotations.AdminRequired;
import com.muyangyo.fileclouddisk.common.aspect.annotations.LocalOperation;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import com.muyangyo.fileclouddisk.manager.service.OperationLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/18
 * Time: 15:59
 */
@RequestMapping("/api/operationLog")
@RestController
@LocalOperation
@AdminRequired
public class OperationLogController {
    @Resource
    private OperationLogService operationLogService;

    @GetMapping("/getLogList")
    public Result getLogList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = 6;
        }

        return Result.success(operationLogService.getLogList(page - 1, pageSize));// page-1 因为前端传过来的是从1开始的
    }

    @DeleteMapping("/deleteLog")
    public Result deleteLog() {
        operationLogService.deleteAllLog();
        return Result.success(true);
    }
}
