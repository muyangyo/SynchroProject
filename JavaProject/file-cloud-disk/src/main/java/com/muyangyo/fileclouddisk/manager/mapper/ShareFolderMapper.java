package com.muyangyo.fileclouddisk.manager.mapper;

import com.muyangyo.fileclouddisk.common.component.MountDirComponent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/16
 * Time: 19:46
 */
@Component
public class ShareFolderMapper {


    @Resource
    private MountDirComponent mountDirComponent;

    public boolean insertNewShareFolder(String path) {
        try {
            LinkedList<String> shareFolders = mountDirComponent.getRootMountDir();

            boolean isChange = false;
            if (!shareFolders.contains(path)) { // 判断是否已经存在
                shareFolders.add(path);
                isChange = true;
            }

            if (isChange) {
                mountDirComponent.insertMountDir(shareFolders);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteShareFolder(String path) {
        try {
            LinkedList<String> shareFolders = mountDirComponent.getRootMountDir();
            shareFolders.remove(path);
            mountDirComponent.insertMountDir(shareFolders);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public LinkedList<String> getShareFolderList() {
        return mountDirComponent.getRootMountDir();
    }
}
