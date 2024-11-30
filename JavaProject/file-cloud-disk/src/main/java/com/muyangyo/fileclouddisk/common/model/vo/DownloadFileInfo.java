package com.muyangyo.fileclouddisk.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/30
 * Time: 17:46
 */
@Data
@AllArgsConstructor
public class DownloadFileInfo {
    private String fileName;
    private String url;
}
