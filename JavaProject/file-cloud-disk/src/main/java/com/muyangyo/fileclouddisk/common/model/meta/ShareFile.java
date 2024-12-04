package com.muyangyo.fileclouddisk.common.model.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareFile {
    private String code; //分享文件的唯一标识
    private String filePath;
    private String creator; //分享文件的创建者
    private Date createTime;
    private Integer status; //默认状态, 1-有效，0-无效
}