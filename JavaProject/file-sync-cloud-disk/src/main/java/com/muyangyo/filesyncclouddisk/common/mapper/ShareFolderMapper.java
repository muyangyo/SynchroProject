package com.muyangyo.filesyncclouddisk.common.mapper;

import com.muyangyo.filesyncclouddisk.common.mapper.wrapped.MountDirMapper;
import com.muyangyo.filesyncclouddisk.common.model.meta.MountDir;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/16
 * Time: 19:46
 * Desc: 这个是封装了 MountDirMapper 的顶层 mapper ，主要是为了方便调用。
 */
@Component
@Slf4j
public class ShareFolderMapper {

    public boolean insertNewShareFolder(String path) {
        try {
            LinkedList<String> shareFolders = getRootMountDir();
            if (shareFolders == null) {
                shareFolders = new LinkedList<>();
            }

            boolean isChange = false;
            // 判断是否已经存在,如果存在，则不再添加
            if (!shareFolders.contains(path)) {
                // 如果不存在，则添加
                shareFolders.add(path);
                isChange = true;
            }

            if (isChange) {
                // 有变更，则插入数据库
                insertMountDir(shareFolders);
            }

            return true;
        } catch (Exception e) {
            log.error("添加新挂载目录中出现异常", e);
            return false;
        }
    }

    public boolean deleteShareFolder(String path) {
        try {
            LinkedList<String> shareFolders = getRootMountDir();

            // 判断是否存在,如果不存在，则不再删除
            if (shareFolders == null) {
                return true;
            }

            shareFolders.remove(path);
            insertMountDir(shareFolders);
            return true;
        } catch (Exception e) {
            log.error("删除挂载目录中出现异常", e);
            return false;
        }
    }

    public LinkedList<String> getShareFolderList() {
        return getRootMountDir() == null ? new LinkedList<>() : getRootMountDir();
    }

    /**
     * 内部方法,用于封装
     */
    @Resource
    MountDirMapper mountDirMapper;

    private LinkedList<String> getRootMountDir() {
        MountDir mountDir = mountDirMapper.select();
        if (mountDir == null) {
            return null;
        }
        return mountDir.getDirList();
    }

    private void insertMountDir(LinkedList<String> dirList) {
        // 规范化路径
        dirList.replaceAll(FileUtils::normalizePath);

        MountDir mountDir = new MountDir();
        mountDir.setDirList(dirList);
        if (getRootMountDir() == null) {
            //如果是第一次写入则插入
            mountDirMapper.insert(mountDir);
        } else {
            // 如果不是第一次写入则更新
            mountDirMapper.update(mountDir);
        }
    }

}
