package com.muyangyo.filesyncclouddisk.common.model.meta;

import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudDiskOperationLog {
    private Date operationTime;
    private String operation;
    private String userName;
    private String userIp;
    private OperationLevel operationLevel;
}