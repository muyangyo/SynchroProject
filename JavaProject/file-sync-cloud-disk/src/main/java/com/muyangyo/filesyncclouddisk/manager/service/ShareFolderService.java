package com.muyangyo.filesyncclouddisk.manager.service;

import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import com.muyangyo.filesyncclouddisk.common.mapper.ShareFolderMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/16
 * Time: 19:45
 */
@Service
@Slf4j
public class ShareFolderService {

    @Resource
    private ShareFolderMapper shareFolderMapper;
    @Resource
    private OperationLogService operationLogService;

    @SneakyThrows
    public Result addNewShareFolder(HttpServletRequest request) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // 设置主题
        JFileChooser fileChooser = new JFileChooser();// 创建文件选择器
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 设置为选择文件夹
        fileChooser.setDialogTitle("选择文件夹"); // 设置对话框标题

        // 显示文件选择对话框
        int result = fileChooser.showOpenDialog(null);

        // 处理用户选择
        if (result == JFileChooser.APPROVE_OPTION) { //用户点击了确定按钮
            File selectedFolder = fileChooser.getSelectedFile();
            String path = FileUtils.getAbsolutePath(selectedFolder); // 格式化绝对路径
            shareFolderMapper.insertNewShareFolder(path);
            operationLogService.addLogFromRequest("管理员操作: 添加共享文件夹[ " + path + " ]", OperationLevel.IMPORTANT, request);
            return Result.success(true);
        } else {
            return Result.fail("用户未选中文件夹");
        }
    }

    public Result getShareFolderList(HttpServletRequest request) {
        operationLogService.addLogFromRequest("管理员操作: 查看挂载路径列表", OperationLevel.IMPORTANT, request);
        return Result.success(shareFolderMapper.getShareFolderList());
    }

    @SneakyThrows
    public Result openFolder(String path, HttpServletRequest request) {
        if (shareFolderMapper.getShareFolderList().contains(path)) {

            // 创建File对象
            File folder = new File(path);

            // 检查文件夹是否存在
            if (!folder.exists() || !folder.isDirectory()) {
                return Result.fail("文件夹不存在!");
            }

            // 检查Desktop是否支持打开文件夹
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                // 打开文件夹
                desktop.open(folder);
                operationLogService.addLogFromRequest("管理员操作: 打开共享文件夹[ " + path + " ]", OperationLevel.WARNING, request);
                return Result.success(true);
            } else {
                log.error("当前平台不支持Desktop API");
                return Result.fail("当前平台不支持Desktop API!");
            }
        }

        return Result.fail("该文件夹不在共享文件夹列表中!");
    }

    public Result deleteShareFolder(String path, HttpServletRequest request) {
        if (shareFolderMapper.getShareFolderList().contains(path)) {
            shareFolderMapper.deleteShareFolder(path);
            operationLogService.addLogFromRequest("管理员操作: 删除共享文件夹[ " + path + " ]", OperationLevel.IMPORTANT, request);
            return Result.success(true);
        } else {
            return Result.fail("该文件夹不在共享文件夹列表中!");
        }
    }

    public Result addNewShareFolderByPath(String path, HttpServletRequest request) {
        File newFolder = new File(path);
        if (newFolder.exists() && newFolder.isDirectory()) {
            shareFolderMapper.insertNewShareFolder(path);
            operationLogService.addLogFromRequest("管理员操作: 添加共享文件夹[ " + path + " ]", OperationLevel.WARNING, request);
            return Result.success(true);
        } else {
            return Result.fail("非法路径!");
        }
    }
}
