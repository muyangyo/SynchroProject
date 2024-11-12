package com.muyangyo.syncfileclouddisk;

import com.muyangyo.syncfileclouddisk.Initer.TrayIconManager;
import com.muyangyo.syncfileclouddisk.exceptions.CanNotCreateFolderException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@SpringBootApplication
@Slf4j
public class SyncFileCloudDiskApplication {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws IOException {
        init();

        // 设置 headless 模式为 false
        context = new SpringApplicationBuilder(SyncFileCloudDiskApplication.class)
                .headless(false)
                .run(args);

        // 在事件调度线程中执行 GUI 相关的操作
        EventQueue.invokeLater(() -> {
            // 获取 TrayIconManager Bean 并初始化托盘图标
            TrayIconManager trayIconManager = context.getBean(TrayIconManager.class);
            trayIconManager.onApplicationReady();
        });
    }

    public static void init() throws IOException {
        File configFile = new File("./config/setting.yml");
        if (!configFile.exists()) {
            log.warn("配置文件不存在, 尝试创建配置文件...");
            File parentFile = configFile.getParentFile();
            if (!parentFile.exists()) {
                // 创建配置文件目录
                boolean ok = parentFile.mkdirs();
                if (!ok) {
                    throw new CanNotCreateFolderException("创建配置文件目录失败!");
                }
            }
            // 复制资源文件到目标路径
            copyResourceToConfigFolder("/setting-default.yml", "./config/setting.yml");
        }
    }

    private static void copyResourceToConfigFolder(String resourcePath, String targetPath) throws IOException {
        // 获取资源文件的 URL
        URL resourceUrl = SyncFileCloudDiskApplication.class.getResource(resourcePath);
        if (resourceUrl == null) {
            throw new IOException("资源文件不存在: " + resourcePath);
        }

        // 目标文件
        File targetFile = new File(targetPath);

        // 获取资源文件的 InputStream
        try (InputStream inputStream = resourceUrl.openStream()) {
            // 复制文件
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
            log.info("成功创建默认配置文件!");
        }
    }

    public static void close() {
        if (context != null) {
            log.warn("关闭程序...");
            int exitCode = SpringApplication.exit(context);
            System.exit(exitCode); // 确保程序完全退出
        }
    }
}