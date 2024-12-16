package com.muyangyo.fileclouddisk.common.component;

import com.muyangyo.fileclouddisk.common.mapper.MountDirMapper;
import com.muyangyo.fileclouddisk.common.model.meta.MountDir;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/25
 * Time: 13:43
 * desk: 挂载目录组件(屏蔽mapper层)
 */
@Component
@Slf4j
public class MountDirComponent {
    @Resource
    MountDirMapper mountDirMapper;

    public LinkedList<String> getRootMountDir() {
        MountDir mountDir = mountDirMapper.select();
        if (mountDir == null) {
            return new LinkedList<>();
        }
        return mountDir.getDirList();
    }

    public void insertMountDir(LinkedList<String> dirList) {
        // 规范化路径
        dirList.replaceAll(FileUtils::normalizePath);

        MountDir mountDir = new MountDir();
        mountDir.setDirList(dirList);
        if (getRootMountDir().isEmpty()) {
            //如果是第一次写入则插入
            mountDirMapper.insert(mountDir);
        } else {
            // 如果不是第一次写入则更新
            mountDirMapper.update(mountDir);
        }
    }

}
