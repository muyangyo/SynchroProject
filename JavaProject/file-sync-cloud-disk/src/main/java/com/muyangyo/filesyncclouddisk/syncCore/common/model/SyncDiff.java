package com.muyangyo.filesyncclouddisk.syncCore.common.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 同步差异报告类
 */
public class SyncDiff {
    private final Set<String> newFiles = new HashSet<>(); // 新增文件
    private final Set<String> modifiedFiles = new HashSet<>(); // 修改文件
    private final Set<String> deletedFiles = new HashSet<>(); // 删除文件

    public void addNewFile(String path) {
        newFiles.add(path);
    }

    public void addModifiedFile(String path) {
        modifiedFiles.add(path);
    }

    public void addDeletedFile(String path) {
        deletedFiles.add(path);
    }

    public Set<String> getNewFiles() {
        return Collections.unmodifiableSet(newFiles);
    }

    public Set<String> getModifiedFiles() {
        return Collections.unmodifiableSet(modifiedFiles);
    }

    public Set<String> getDeletedFiles() {
        return Collections.unmodifiableSet(deletedFiles);
    }
}