package com.muyangyo.fileclouddisk.common.model.meta;

import com.muyangyo.fileclouddisk.common.model.enums.OperationLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLog {
    private Date operationTime;
    private String operation;
    private String userName;
    private String userIp;
    private OperationLevel operationLevel;
}