package com.muyangyo.fileclouddisk.common.model.vo;

import converter.NoNeedMetaMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/6
 * Time: 20:53
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecycleBinFileVO {
    private String id;
    private String fileName;
    private String fileOriginalPath;
    @NoNeedMetaMapping
    private String deleteTime;

    private String remainingTime;
}
