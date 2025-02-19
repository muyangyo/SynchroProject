package com.muyangyo.filesyncclouddisk.syncCore.common.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 同步差异报告类
 */
public class SyncDiff {
    private final Set<String> needUploadFiles = new HashSet<>(); // 需要上传的文件
    private final Set<String> needDownloadFiles = new HashSet<>(); // 需要下载的文件
    private final Set<String> needDeletedFiles = new HashSet<>(); // 删除文件

    public void addUploadFile(String path) {
        needUploadFiles.add(path);
    }

    public void addDownloadFile(String path) {
        needDownloadFiles.add(path);
    }

    public void addDeletedFile(String path) {
        needDeletedFiles.add(path);
    }

    public Set<String> getNeedUploadFiles() {
        return Collections.unmodifiableSet(needUploadFiles);
    }

    public Set<String> getNeedDownloadFiles() {
        return Collections.unmodifiableSet(needDownloadFiles);
    }

    public Set<String> getNeedDeletedFiles() {
        return Collections.unmodifiableSet(needDeletedFiles);
    }
}