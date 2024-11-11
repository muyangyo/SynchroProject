package com.muyangyo.syncfileclouddisk;

import com.muyangyo.syncfileclouddisk.exceptions.CanNotCreateFolderException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@SpringBootApplication
@Slf4j
public class SyncFileCloudDiskApplication {
    private static ApplicationContext context;

    public static void main(String[] args) throws IOException {
        init();
        context = SpringApplication.run(SyncFileCloudDiskApplication.class, args);
    }


    public static void init() throws IOException {
        File configFile = new File("./config/setting.yml");
        if (!configFile.exists()) {
            log.warn("配置文件不存在, 尝试创建配置文件...");
            // 创建配置文件目录
            boolean ok = configFile.getParentFile().mkdirs();
            if (!ok) {
                throw new CanNotCreateFolderException("创建配置文件目录失败!");
            }

            // 复制资源文件到目标路径

            // 获取资源文件的 URL
            URL resourceUrl = SyncFileCloudDiskApplication.class.getResource("/setting-default.yml");

            // 目标文件
            File targetFile = new File("./config/setting.yml");

            // 获取资源文件的 InputStream
            assert resourceUrl != null;
            try (InputStream inputStream = resourceUrl.openStream()) {
                // 复制文件
                FileUtils.copyInputStreamToFile(inputStream, targetFile);
                log.info("成功创建默认配置文件!");
            }
        }
    }


    public static void close() {
        SpringApplication.exit(context);
    }

}
