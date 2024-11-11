package com.muyangyo.syncfileclouddisk.Initer;

import com.muyangyo.syncfileclouddisk.SyncFileCloudDiskApplication;
import com.muyangyo.syncfileclouddisk.config.Setting;
import com.muyangyo.syncfileclouddisk.model.enumeration.SystemType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.prefs.Preferences;

@Service
@Slf4j
public class TrayIconManager {
    @Autowired
    private Setting setting; // 注入配置设置

    // 应用启动时触发的事件
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        // 检查系统是否支持托盘图标
        if (!SystemTray.isSupported()) {
            log.info("系统不支持托盘图标");
            return;
        }

        // 启动托盘图标
        try {
            SystemTray systemTray = SystemTray.getSystemTray(); // 获取系统托盘
            TrayIcon trayIcon = createTrayIcon(); // 创建托盘图标
            systemTray.add(trayIcon); // 将托盘图标添加到系统托盘
        } catch (AWTException e) {
            e.printStackTrace(); // 捕获并打印异常
        }
    }

    // 创建托盘图标和右键菜单的方法
    private TrayIcon createTrayIcon() {
        // 托盘图标和菜单项
        Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/icon.jpg")); // 加载托盘图标
        PopupMenu popupMenu = new PopupMenu(); // 创建右键弹出菜单

        // 右键菜单项：打开项目网址
        MenuItem openUrlItem = new MenuItem("打开项目网址");
        openUrlItem.addActionListener(e -> openUrl()); // 添加点击事件
        popupMenu.add(openUrlItem); // 将菜单项添加到弹出菜单

        if (setting.getSystemType() == SystemType.WINDOWS) {
            // 右键菜单项：注册开机自启
            MenuItem registerStartupItem = new MenuItem("开机自启");
            registerStartupItem.addActionListener(e -> registerAutoStartup()); // 添加点击事件
            popupMenu.add(registerStartupItem); // 将菜单项添加到弹出菜单
        }

        // 分割线
        popupMenu.addSeparator();

        // 右键菜单项：退出
        MenuItem exitItem = new MenuItem("退出");
        exitItem.addActionListener(e -> exitApplication()); // 添加点击事件
        popupMenu.add(exitItem); // 将菜单项添加到弹出菜单

        // 创建托盘图标并设置弹出菜单
        TrayIcon trayIcon = new TrayIcon(image, "SyncFileCloudDisk", popupMenu);

        // 左键点击：打开项目网址
        trayIcon.addActionListener(e -> openUrl());

        return trayIcon; // 返回托盘图标
    }

    // 打开项目网址的方法
    private void openUrl() {
        try {
            Desktop.getDesktop().browse(java.net.URI.create(setting.getAbsoluteUrl())); // 使用系统浏览器打开网址
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 退出应用程序的方法
    private void exitApplication() {
        SyncFileCloudDiskApplication.close(); // 调用应用的关闭方法
    }

    // 注册开机自启方法
    private void registerAutoStartup() {
        // 仅支持 Windows 系统开机自启
        SystemType systemType = setting.getSystemType();
        if (systemType == SystemType.WINDOWS) {
            registerWindowsStartup();
        } else {
            JOptionPane.showMessageDialog(null, "当前系统不支持开机自启功能");
        }
    }

    /**
     * 在 Windows 上注册开机自启
     */
    private void registerWindowsStartup() {
        // TODO: 2024/11/11 注册开机自启功能验证
        String appPath = System.getProperty("user.dir") + "\\" + setting.getApplicationName() + ".exe"; // 获取应用路径（修改为 .exe 文件）
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName()); // 获取用户根节点的偏好设置
        // 将应用路径添加到 Windows 注册表
        prefs.put(setting.getApplicationName(), appPath);
        JOptionPane.showMessageDialog(null, "Windows系统开机自启注册成功!"); // 弹出成功提示
    }
}
