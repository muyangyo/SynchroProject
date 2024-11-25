package com.muyangyo.fileclouddisk.user.service;

import com.muyangyo.fileclouddisk.common.component.MountDirComponent;
import com.muyangyo.fileclouddisk.common.model.other.FileType;
import com.muyangyo.fileclouddisk.common.model.vo.FileInfo;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/25
 * Time: 13:25
 */
@Slf4j
@Service
public class FileService {

    @Resource
    private MountDirComponent mountDirComponent;

    /**
     * 获取根目录下的文件信息
     *
     * @return 文件信息列表
     */
    public LinkedList<FileInfo> getRootFileInfo() throws IOException {
        List<String> dirList = mountDirComponent.getRootMountDir();
        // 遍历目录，获取目录信息
        return getFileInfoListFromList(dirList);
    }

    /**
     * 获取指定目录下的文件信息
     *
     * @param path 目录路径
     * @return 文件信息列表
     */
    @SneakyThrows
    public LinkedList<FileInfo> getNormalFileInfo(String path) {
        List<String> dirList = mountDirComponent.getRootMountDir();
        // 保证file路径在根目录下
        boolean ok = false;
        for (String root : dirList) {
            // 判断是否是根目录本身或者子目录
            if (path.equals(root) || path.startsWith(root + "/")) {
                ok = true;
                break;
            }
        }
        if (!ok) { // 路径不在根目录下
            return null;
        }

        // 遍历目录，获取目录信息
        File file = new File(path);
        List<String> list = FileUtils.listFilesInDirectory(file);

        return getFileInfoListFromList(list);
    }

    /**
     * 获取指定目录下的文件信息
     *
     * @param file 目录
     * @return 文件信息列表
     */
    private FileInfo initFileInfo(File file) throws IOException {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(FileUtils.getFileName(file));
        fileInfo.setFileSize(file.isDirectory() ? -1 : file.length());
        fileInfo.setModifiedTime(FileUtils.getFileModifiedTime(file));
        fileInfo.setFilePath(file.getAbsolutePath());
        fileInfo.setFileType(FileType.getFileTypeFromFile(file));
        return fileInfo;
    }

    private LinkedList<FileInfo> getFileInfoListFromList(List<String> list) throws IOException {
        LinkedList<FileInfo> retList = new LinkedList<>();
        for (String s : list) {
            File f = new File(s);
            FileInfo fileInfo = initFileInfo(f);
            retList.add(fileInfo);
        }
        return retList;
    }
}
