package com.muyangyo.fileclouddisk.common.model.enums;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/13
 * Time: 21:25
 */
public enum CommonSizeUnit {
    BYTE(1L),
    KB(1024L),
    MB(1024L * 1024L),
    GB(1024L * 1024L * 1024L),
    TB(1024L * 1024L * 1024L * 1024L);

    private final long size;

    private CommonSizeUnit(long size) {
        this.size = size;
    }

    public long getSize() {
        return size;
    }
}
