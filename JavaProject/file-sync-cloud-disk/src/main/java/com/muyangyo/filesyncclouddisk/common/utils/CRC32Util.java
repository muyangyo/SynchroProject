package com.muyangyo.filesyncclouddisk.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class CRC32Util {

    /**
     * 计算文件的CRC32校验和
     *
     * @param file 文件对象
     * @return CRC32校验和
     */
    public static long calculateCRC32(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            CRC32 crc = new CRC32();
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                crc.update(buffer, 0, bytesRead);
            }
            return crc.getValue();
        }
    }
}