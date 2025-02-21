package com.muyangyo.filesyncclouddisk.common.initializer;

import cn.hutool.core.util.RandomUtil;
import com.muyangyo.filesyncclouddisk.FileSyncCloudDiskApplication;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.exception.CanNotCreateFolderException;
import com.muyangyo.filesyncclouddisk.common.exception.InitFailedException;
import com.muyangyo.filesyncclouddisk.common.model.enums.SystemType;
import com.muyangyo.filesyncclouddisk.common.utils.DeviceIdGenerator;
import com.muyangyo.filesyncclouddisk.common.utils.NetworkUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URL;

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
public class ConfigInitializer {
    @Resource
    private Setting setting;//postStruct时就有

    @Override
    public String toString() {
        return setting.getPort().toString();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printSystemInfo() {
        SystemType systemType = setting.getSystemType();
        if (systemType.equals(SystemType.OTHER)) {
            log.error("系统类型不受支持,目前只支持Windows、Linux系统");
            FileSyncCloudDiskApplication.close();
            throw new InitFailedException("系统类型不受支持,目前只支持Windows、Linux系统");
        }
        log.info("成功识别系统类型: {} ", systemType);
        log.info("网页服务成功启动于: " + setting.getCompletePublicServerURL());
        log.info("本地服务地址为: " + setting.getCompleteLocalServerURL());
    }

    public static void init() throws IOException {
        File configFile = new File("./config/setting.yml");
        boolean isFirstStart = false; // 是否是第一次启动
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
            writeConfigsToYml(configFile, generateRandomSignature(), DeviceIdGenerator.generateDeviceId(), RandomUtil.randomString(8));// 生成随机签名并写入配置文件
            //没有配置文件则默认是第一次启动,则需要打开管理页面
            isFirstStart = true;
        }
        // 看看是不是开发模式
        boolean isDev = getDevMode(configFile);
        if (isDev) {
            log.warn("当前处于开发模式!");
        }

        // 判断程序是否已经启动或者说端口是否被占用
        int port = getServerPort(configFile);
        boolean isSupportBrowser = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE);
        String url = isFirstStart ? ("http://127.0.0.1" + ":" + port + Setting.MANAGER_RELATIVE_PATH) : ("http://" + NetworkUtils.getServerIP() + ":" + port + Setting.USER_RELATIVE_PATH);
        log.info("正在启动服务, 监听端口: " + port);
        if (isPortInUse(port)) {
            if (isSupportBrowser && !isDev) {
                try {
                    Desktop.getDesktop().browse(java.net.URI.create(url)); // 打开浏览器
                } catch (IOException e) {
                    log.error("打开浏览器失败: {}", e.getMessage());
                }
            }
            log.error("端口已被占用或者已经启动!!!请检查端口配置!");
            System.exit(0);
            return;
        }

        //如果端口号没有被占用,则正常启动服务,根据是否是第一次启动打开不同的页面
        if (isSupportBrowser && !isDev) {
            try {
                Desktop.getDesktop().browse(java.net.URI.create(url)); // 打开浏览器
                log.trace("打开浏览器成功 [{}]", url);
            } catch (IOException e) {
                log.error("打开浏览器失败: {}", e.getMessage());
            }
        }

        // 清理缓存文件夹
        File downloadTempFolder = new File(Setting.USER_DOWNLOAD_TEMP_DIR_PATH);
        if (downloadTempFolder.exists()) {
            log.info("清理下载缓存文件夹: {}", downloadTempFolder.getPath());
            com.muyangyo.filesyncclouddisk.common.utils.FileUtils.delete(downloadTempFolder);
        }
        // 清理上传缓存文件夹
        File uploadTempFolder = new File(Setting.USER_UPLOAD_TEMP_DIR_PATH);
        if (uploadTempFolder.exists()) {
            log.info("清理上传缓存文件夹: {}", uploadTempFolder.getPath());
            com.muyangyo.filesyncclouddisk.common.utils.FileUtils.delete(uploadTempFolder);
        }
    }

    /**
     * 从 YAML 文件中获取服务器端口
     *
     * @param yamlFile YAML 文件对象
     * @return 服务器端口，默认返回 8080
     */
    public static int getServerPort(File yamlFile) throws IOException {
        String content = com.muyangyo.filesyncclouddisk.common.utils.FileUtils.readFileToStr(yamlFile.getPath());
        int indexOfPort = content.indexOf("port:") + 6;
        String substring = content.substring(indexOfPort, content.indexOf(" ", indexOfPort));
        return Integer.parseInt(substring.trim());
    }

    /**
     * 从 YAML 文件中判断是否是开发模式
     *
     * @param yamlFile YAML 文件对象
     * @return 是否是开发模式，默认返回 false
     */
    public static boolean getDevMode(File yamlFile) throws IOException {
        String content = com.muyangyo.filesyncclouddisk.common.utils.FileUtils.readFileToStr(yamlFile.getPath());
        int index = content.indexOf("dev:") + 5;
        String substring = content.substring(index, content.indexOf(" ", index));
        return Boolean.parseBoolean(substring.trim());
    }

    /**
     * 检查端口是否被占用
     */
    private static boolean isPortInUse(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            return false; // 端口未被占用
        } catch (IOException e) {
            return true; // 端口已被占用
        }
    }

    private static void writeConfigsToYml(File configFile, String signature, String deviceID, String certificatePassword) throws IOException {
        // 读取文件内容
        String content = com.muyangyo.filesyncclouddisk.common.utils.FileUtils.readFileToStr(configFile.getPath());
        // 替换签名
        content = content.replace("${sign}", signature);
        // 替换设备ID
        content = content.replace("${deviceID}", deviceID);
        //替换证书密码
        content = content.replace("${certificatePassword}", certificatePassword);
        // 写入文件
        com.muyangyo.filesyncclouddisk.common.utils.FileUtils.writeFileFromStr(configFile.getPath(), content, false);
        log.info("成功生成并写入 token 签名,设备ID,证书密码到配置文件!");
    }

    private static String generateRandomSignature() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Encoders.BASE64.encode(key.getEncoded());
    }

    private static void copyResourceToConfigFolder(String resourcePath, String targetPath) throws IOException {
        // 获取资源文件的 URL
        URL resourceUrl = FileSyncCloudDiskApplication.class.getResource(resourcePath);
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
}
