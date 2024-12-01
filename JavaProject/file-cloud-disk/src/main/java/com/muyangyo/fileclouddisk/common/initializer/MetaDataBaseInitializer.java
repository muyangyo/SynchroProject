package com.muyangyo.fileclouddisk.common.initializer;

import com.muyangyo.fileclouddisk.FileCloudDiskApplication;
import com.muyangyo.fileclouddisk.common.mapper.CreateTablesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;
import java.io.File;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/23
 * Time: 14:17
 */
@Slf4j
@Configuration
public class MetaDataBaseInitializer {
    @Resource
    private CreateTablesMapper createTablesMapper;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        File dbFile = new File("./data/meta.db");
        if (!dbFile.exists()) {
            log.info("数据库文件不存在，创建中...");

            if (!dbFile.getParentFile().exists()) {
                if (!dbFile.getParentFile().mkdirs()) {
                    log.error("创建数据库文件失败！");
                    FileCloudDiskApplication.close();
                }
            }

            if (!createTables()) {
                log.error("创建数据库表失败！");
                FileCloudDiskApplication.close();
            }

            log.info("数据库初始化完成！");
        }
    }

    private Boolean createTables() {
        try {
            createTablesMapper.createMountDirsTable();
            createTablesMapper.createAdminTable();
            createTablesMapper.createUserTable();
            createTablesMapper.createShareFileTable();
            return true;
        } catch (Exception e) {
            log.error("创建数据库表失败！", e);
            return false;
        }
    }

}
