package com.muyangyo.fileclouddisk.common.initializer;

import com.muyangyo.fileclouddisk.FileCloudDiskApplication;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.exception.CanNotCreateFolderException;
import com.muyangyo.fileclouddisk.common.exception.InitFailedException;
import com.muyangyo.fileclouddisk.common.model.enums.SystemType;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
            FileCloudDiskApplication.close();
            throw new InitFailedException("系统类型不受支持,目前只支持Windows、Linux系统");
        }
        log.info("成功识别系统类型: {} ", systemType);
        log.info("网页服务成功启动于: " + setting.getCompleteServerURL());
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
            writeSignatureToYml(configFile, generateRandomSignature());// 生成随机签名并写入配置文件
        }
    }

    private static void writeSignatureToYml(File configFile, String signature) throws IOException {
        // 读取文件内容
        String content = com.muyangyo.fileclouddisk.common.utils.FileUtils.readFileToStr(configFile.getPath());
        // 替换签名
        content = content.replace("${sign}", signature);
        // 写入文件
        com.muyangyo.fileclouddisk.common.utils.FileUtils.writeFileFromStr(configFile.getPath(), content, false);
        log.info("成功生成并写入token签名");
    }

    private static String generateRandomSignature() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return Encoders.BASE64.encode(key.getEncoded());
    }

    private static void copyResourceToConfigFolder(String resourcePath, String targetPath) throws IOException {
        // 获取资源文件的 URL
        URL resourceUrl = FileCloudDiskApplication.class.getResource(resourcePath);
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
