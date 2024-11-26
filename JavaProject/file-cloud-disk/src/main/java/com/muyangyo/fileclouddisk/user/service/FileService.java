package com.muyangyo.fileclouddisk.user.service;

import com.muyangyo.fileclouddisk.common.component.MountDirComponent;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.exception.IllegalPath;
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
    private static final int REAL_PATH_INDEX = 0;
    private static final int REAL_MOUNT_ROOT_PATH_INDEX = 1;
    @Resource
    private MountDirComponent mountDirComponent;

    /**
     * 获取指定目录下的文件信息
     *
     * @param path 目录路径
     * @return 文件信息列表
     */
    @SneakyThrows
    public LinkedList<FileInfo> getFileInfoList(String path) {
        if (path.equals(Setting.FE_USER_BASE_URL)) return getRootFileInfo();// 根目录
        // 将path转为绝对路径
        LinkedList<String> linkedList = fakePathConverterToRealPath(path);
        // 遍历目录，获取目录信息
        File file = new File(linkedList.get(REAL_PATH_INDEX));
        List<String> list = FileUtils.listFilesInDirectory(file);

        LinkedList<FileInfo> retList = new LinkedList<>();
        for (String s : list) {
            retList.add(initFileInfo(new File(s), linkedList.get(REAL_MOUNT_ROOT_PATH_INDEX)));
        }
        return retList;
    }

    /**
     * 判断是否在根目录下,并获取文件信息
     *
     * @param file 文件
     * @return 文件信息
     * @throws IllegalPath 路径不在根目录下
     */
    public FileInfo checkFollowRootPathAndGetFileInfo(File file) throws IllegalPath {
        String path = FileUtils.getAbsolutePath(file);
        List<String> dirList = mountDirComponent.getRootMountDir();
        String curRootPath = "";
        boolean ok = false;
        for (String dir : dirList) {
            if (path.equals(dir) || path.equals(dir + "/")) {
                curRootPath = dir;
                ok = true;
                break;
            }
        }
        if (!ok) throw new IllegalPath("非法路径!该路径不存在于挂载目录中!");
        return initFileInfo(file, curRootPath);
    }

    /**
     * 获取根目录下的文件信息
     *
     * @return 文件信息列表
     */
    private LinkedList<FileInfo> getRootFileInfo() throws IOException {
        List<String> dirList = mountDirComponent.getRootMountDir();
        // 遍历目录，获取目录信息

        LinkedList<FileInfo> retList = new LinkedList<>();
        for (String root : dirList) {
            File file = new File(root);
            retList.add(initFileInfo(file, root));
        }
        return retList;
    }

    /**
     * 假路径转换为真路径
     *
     * @param path 假路径
     * @return 包含实际完整路径和实际挂载根目录的链表
     */
    private LinkedList<String> fakePathConverterToRealPath(String path) throws IllegalPath {
        int indexOfSecondSlash = path.indexOf("/", 1);// 第二个斜杠的位置
        path = path.substring(indexOfSecondSlash, path.length()); // 截取第二个斜杠之后的路径(第一个斜杠的内容是前端用的,后面的才对后端有用)
        String[] strings = path.split("/");
        String mountRootPathFromPath = strings[1];// 挂载根目录的结尾路径

        String realMountRootPath = "";// 实际根目录的结尾路径
        boolean ok = false;// 是否找到了挂载根目录
        List<String> dirList = mountDirComponent.getRootMountDir();
        for (String root : dirList) {
            if (root.endsWith(mountRootPathFromPath)) {
                ok = true;
                realMountRootPath = root;
                break;
            }
        }
        if (!ok) { // 路径不在根目录下
            throw new IllegalPath("非法路径!该路径不存在于挂载目录中!");
        }
        String realPath = realMountRootPath + path.replace("/" + mountRootPathFromPath, "");// 实际路径
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add(realPath);
        linkedList.add(realMountRootPath);
        return linkedList;
    }

    /**
     * 获取指定目录下的文件信息
     *
     * @param file 目录
     * @return 文件信息列表
     */
    @SneakyThrows
    private FileInfo initFileInfo(File file, String mountRootPath) {
        if (mountRootPath.equals("")) {
            throw new IllegalPath("挂载根目录为空!");
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(FileUtils.getFileName(file));
        fileInfo.setFileSize(file.isDirectory() ? -1 : file.length());
        fileInfo.setModifiedTime(FileUtils.getFileModifiedTime(file));
        fileInfo.setFilePath(FileUtils.getAbsolutePath(file));
        fileInfo.setMountRootPath(mountRootPath);
        fileInfo.setFileType(FileType.getFileTypeFromFile(file));
        return fileInfo;
    }
}
