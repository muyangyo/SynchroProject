package com.muyangyo.fileclouddisk.common.model.vo;

import lombok.Getter;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/26
 * Time: 21:24
 */

@Getter
public class VideoFileInfo extends FileInfo {
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }
}
