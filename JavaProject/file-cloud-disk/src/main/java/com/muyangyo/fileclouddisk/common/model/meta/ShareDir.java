package com.muyangyo.fileclouddisk.common.model.meta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.LinkedList;

@Data
public class ShareDir {
    private LinkedList<String> dir; // 共享目录路径

    /**
     * 将目录路径转换为json字符串 (数据库存储)
     *
     * @return json字符串
     */
    public String getDir() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(dir);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json字符串转换为目录路径 (数据库读取)
     *
     * @param dir json字符串
     */
    public void setDir(String dir) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.dir = mapper.readValue(dir, new TypeReference<LinkedList<String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取目录路径列表 (用于前端显示)
     *
     * @return 目录路径列表
     */
    public LinkedList<String> getDirList() {
        return this.dir;
    }
}