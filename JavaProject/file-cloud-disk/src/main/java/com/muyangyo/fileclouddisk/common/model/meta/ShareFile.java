package com.muyangyo.fileclouddisk.common.model.meta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Date;
import java.util.LinkedList;

@Data
public class ShareFile {
    private String id; //分享文件的id(字符串长10)
    private LinkedList<String> fileList;
    private String creator; //分享文件的创建者
    private Date createTime;
    private Integer status = 1; //默认状态, 1-有效，0-无效

    /**
     * 获取分享文件的文件列表(给数据库的)
     */
    public String getFileList() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(fileList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置分享文件的文件列表(给数据库的)
     */
    public void setFileList(String fileList) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.fileList = mapper.readValue(fileList, new TypeReference<LinkedList<String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分享文件的文件列表
     *
     * @return 分享文件的文件列表
     */
    public LinkedList<String> getShareFileList() {
        return fileList;
    }

    /**
     * 设置分享文件的文件列表
     *
     * @param fileList 分享文件的文件列表
     */
    public void setShareFileList(LinkedList<String> fileList) {
        this.fileList = fileList;
    }
}