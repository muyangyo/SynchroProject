package com.muyangyo.filesyncclouddisk.common.utils;

import com.muyangyo.filesyncclouddisk.common.model.enums.SystemType;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/20
 * Time: 19:24
 */
@Slf4j
public class SSLUtils {
    /**
     * 生成证书文件,默认生成在当前目录下
     *
     * @param password         证书密钥
     * @param keystoreFileName keystore 文件名
     * @return true 证书文件生成成功，false 证书文件生成失败
     */
    public static boolean autoGenerateKeystore(String password, String keystoreFileName) {
        try {
            // 1. 定义 keytool 命令
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "keytool", "-genkey", "-alias", "ftpserver", "-keyalg", "RSA",
                    "-keystore", "keystore.jks", "-keysize", "2048", "-validity", "3650",
                    "-storepass", password, "-keypass", password,
                    "-dname", "CN=Server, OU=IT, O=Owner, L=Beijing, ST=Beijing, C=CN"
            );

            // 2. 启动进程
            Process process = processBuilder.start();

            // 3. 读取命令输出
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), OSUtils.getSystemType() == SystemType.WINDOWS ? "GBK" : "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }


            // 4. 等待命令执行完成
            int exitCode = process.waitFor();

            // 5. 检查生成的 keystore.jks 文件
            File keystoreFile = new File(keystoreFileName);
            if (keystoreFile.exists()) {
                log.info("keystore.jks 文件已生成，路径：" + keystoreFile.getAbsolutePath());
                return true;
            } else {
                log.error("keystore.jks 文件未生成");
                return false;
            }
        } catch (Exception e) {
            log.error("自动生成 keystore.jks 文件失败", e);
            return false;
        }
    }
}
