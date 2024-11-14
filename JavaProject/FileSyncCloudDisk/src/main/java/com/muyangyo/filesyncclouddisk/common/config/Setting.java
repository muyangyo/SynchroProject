package com.muyangyo.filesyncclouddisk.common.config;

import com.muyangyo.filesyncclouddisk.FileSyncCloudDiskApplication;
import com.muyangyo.filesyncclouddisk.common.exception.InitFailedException;
import com.muyangyo.filesyncclouddisk.common.model.enumeration.SystemType;
import com.muyangyo.filesyncclouddisk.common.utils.EasyTimedCache;
import com.muyangyo.filesyncclouddisk.common.utils.NetworkUtils;
import com.muyangyo.filesyncclouddisk.common.utils.OSUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.security.PrivateKey;
import java.util.stream.Stream;

@Configuration
@Slf4j
@Getter
public class Setting {

    @Value("${server.port}")
    private Integer port;
    @Value("${spring.application.name}")
    private String applicationName;

    private SystemType systemType;
    private String localIp;
    private String absoluteUrl;
    @Value("${invitationCode}")
    private String invitationCode;

    private EasyTimedCache<String, Integer> loginAndRegisterTimeCache;
    private EasyTimedCache<String, PrivateKey> rasCache;
    @Value("${maxNumberOfAttempts}")
    private Integer maxNumberOfAttempts;

    private static final long LIMIT_TIME = 5 * 60 * 1000; // 5分钟内登录和注册次数限制
    private static final int CACHE_SIZE = 10; // 临时缓存大小
    private static final long RSA_USEFUL_TIME = 5 * 60 * 1000; // 5分钟内RSA密钥有效期


    @PostConstruct
    public void init() {
        this.systemType = OSUtils.getSystemType();
        this.localIp = NetworkUtils.getServerIP();
        this.absoluteUrl = "http://" + localIp + ":" + port;
        this.loginAndRegisterTimeCache = new EasyTimedCache<>(CACHE_SIZE, LIMIT_TIME, true);
        this.rasCache = new EasyTimedCache<>(CACHE_SIZE, RSA_USEFUL_TIME, true);

        boolean isConfigInvalid = Stream.of
                        (port, applicationName, systemType, localIp, absoluteUrl, invitationCode, loginAndRegisterTimeCache, rasCache, maxNumberOfAttempts)
                .anyMatch(value -> value == null || (value instanceof String && !StringUtils.hasLength((String) value)));

        if (isConfigInvalid) {
            log.error("初始化项目配置失败，请检查配置!");
            FileSyncCloudDiskApplication.close();
            throw new InitFailedException("初始化项目配置失败，请检查配置!");
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("正在释放缓存...");
        loginAndRegisterTimeCache.shutdown();
        rasCache.shutdown();
        log.info("缓存释放完毕!");
    }
}