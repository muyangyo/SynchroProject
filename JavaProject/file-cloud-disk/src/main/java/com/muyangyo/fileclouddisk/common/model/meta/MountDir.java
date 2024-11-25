package com.muyangyo.fileclouddisk.common.model.meta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedList;

public class MountDir {
    private LinkedList<String> mountDir; // 共享目录路径

    /**
     * 将目录路径转换为json字符串 (数据库存储)
     *
     * @return json字符串
     */
    public String getMountDir() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(mountDir);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json字符串转换为目录路径 (数据库读取)
     *
     * @param mountDir json字符串
     */
    public void setMountDir(String mountDir) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.mountDir = mapper.readValue(mountDir, new TypeReference<LinkedList<String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取目录路径列表
     *
     * @return 目录路径列表
     */
    public LinkedList<String> getDirList() {
        return this.mountDir;
    }

    /**
     * 设置目录路径列表
     *
     * @param list 目录路径列表
     */
    public void setDirList(LinkedList<String> list) {
        this.mountDir = list;
    }
}