package com.muyangyo.syncfileclouddisk.Initer;

import com.muyangyo.syncfileclouddisk.SyncFileCloudDiskApplication;
import com.muyangyo.syncfileclouddisk.config.Setting;
import com.muyangyo.syncfileclouddisk.exceptions.InitFailedException;
import com.muyangyo.syncfileclouddisk.model.enumeration.SystemType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 9:55
 */
@Configuration
@Data
@Slf4j
public class StartManager {

    @Autowired
    private Setting setting;

    @Override
    public String toString() {
        return setting.getPort().toString();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        SystemType systemType = setting.getSystemType();
        if (systemType.equals(SystemType.OTHER)) {
            log.error("系统类型不受支持,目前只支持Windows、Linux系统");
            SyncFileCloudDiskApplication.close();
            throw new InitFailedException("系统类型不受支持,目前只支持Windows、Linux系统");
        }
        log.info("成功识别系统类型: {} ", systemType);
        log.info("网页服务成功启动于: " + setting.getAbsoluteUrl());
    }
}
