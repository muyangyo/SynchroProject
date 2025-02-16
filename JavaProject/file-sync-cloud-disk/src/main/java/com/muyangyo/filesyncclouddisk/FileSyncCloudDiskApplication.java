package com.muyangyo.filesyncclouddisk;

import com.muyangyo.filesyncclouddisk.common.initializer.ConfigInitializer;
import com.muyangyo.filesyncclouddisk.common.initializer.TrayIconInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;
import java.io.IOException;

@SpringBootApplication
@Slf4j
public class FileSyncCloudDiskApplication {

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) throws IOException {
        ConfigInitializer.init();// 初始化外部文件

        // 设置 headless 模式为 false 以显示 GUI
        context = new SpringApplicationBuilder(FileSyncCloudDiskApplication.class)
                .headless(false)
                .run(args);

        // 在事件调度线程中执行 GUI 相关的操作
        EventQueue.invokeLater(() -> {
            // 获取 TrayIconManager Bean 并初始化托盘图标
            TrayIconInitializer trayIconManager = context.getBean(TrayIconInitializer.class);
            trayIconManager.onApplicationReady();
        });
    }

    public static void close() {
        if (context != null) {
            log.warn("关闭程序...");
            int exitCode = SpringApplication.exit(context);
            System.exit(exitCode); // 确保程序完全退出
        }
    }

}
