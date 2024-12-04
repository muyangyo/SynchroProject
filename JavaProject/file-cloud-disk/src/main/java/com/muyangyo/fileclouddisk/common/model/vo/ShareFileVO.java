package com.muyangyo.fileclouddisk.common.model.vo;

import converter.NoNeedMetaMapping;
import lombok.Data;

@Data
public class ShareFileVO {
    private String code; //分享文件的唯一标识
    private String filePath;
    @NoNeedMetaMapping
    private String createTime;
    private Integer status; //默认状态, 1-有效，0-无效
}