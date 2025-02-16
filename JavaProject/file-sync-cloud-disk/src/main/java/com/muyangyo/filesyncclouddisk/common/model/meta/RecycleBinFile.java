package com.muyangyo.filesyncclouddisk.common.model.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecycleBinFile {
    private String id;
    private String fileName;
    private String fileOriginalPath;
    private Date deleteTime;
}