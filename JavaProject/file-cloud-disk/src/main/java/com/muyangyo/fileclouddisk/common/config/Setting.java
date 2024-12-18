package com.muyangyo.fileclouddisk.common.config;


import com.muyangyo.fileclouddisk.FileCloudDiskApplication;
import com.muyangyo.fileclouddisk.common.exception.InitFailedException;
import com.muyangyo.fileclouddisk.common.model.enums.SystemType;
import com.muyangyo.fileclouddisk.common.model.meta.ShareFile;
import com.muyangyo.fileclouddisk.common.utils.*;
import com.muyangyo.fileclouddisk.user.mapper.ShareFileMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
    private String serverIP;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private String completeServerURL;
    @Value("${invitationCode}")
    private String invitationCode;

    @Value("${token.lifeTime}")
    private int tokenLifeTime;
    @Value("${token.signature}")
    private String signature;

    @Value("${securityOptions.userComponent}")
    private boolean loginAndRegisterUseEncrypt;

/*    @Value("${securityOptions.dataComponent}") todo: 待完善
  dataComponent: false # 文件加密(不建议开启,消耗性能过大)
    private boolean dataUseEncrypt; // 文件数据是否加密*/

    @Value("${securityOptions.localOperationOnly}")
    private boolean localOperationOnly;

    private EasyTimedCache<String, Integer> loginAndRegisterTimeCache; // 登录和注册时间缓存
    private EasyTimedCache<String, PrivateKey> rasCache; // RSA密钥缓存
    @Value("${maxNumberOfAttempts}")
    private Integer maxNumberOfAttempts;// 登录和注册最大尝试次数
    private static final long LIMIT_TIME = 5 * 60 * 1000; // 5分钟内登录和注册次数限制
    private static final int CACHE_SIZE = 10; // 临时缓存大小
    private static final long RSA_USEFUL_TIME = 5 * 60 * 1000; // 5分钟内RSA密钥有效期

    //文件模块的配置
    private ConcurrentLRUCache<String, File> videoCache; // 视频缓存
    private ConcurrentLRUCache<String, File> fileCache; // 文件缓存
    @Value("${Cache.videoCacheSize}")
    private int videoCacheSize; // 视频缓存大小
    @Value("${Cache.downloadFileCacheSize}")
    private int downloadFileCacheSize;// 文件缓存大小
    @Value("${Cache.downloadFileCacheExpireTime}")
    private int downloadFileCacheExpireTime; // 文件缓存过期时间(分钟)
    private CustomTimer customTimer; // 用于定时清理压缩包的
    @Value("${Cache.MaximumSurvivalTimeOfSharedFile}")
    private int maximumSurvivalTimeOfSharedFile; // 共享文件最大存活时间(分钟)

    // 全局
    private ExecutorService settingThreadPool; // 线程池

    @PostConstruct
    public void init() {
        this.systemType = OSUtils.getSystemType();
        this.serverIP = NetworkUtils.getServerIP();
        this.completeServerURL = "http://" + serverIP + ":" + port + contextPath;
        this.loginAndRegisterTimeCache = new EasyTimedCache<>(CACHE_SIZE, LIMIT_TIME, true);// 登录和注册时间缓存
        this.rasCache = new EasyTimedCache<>(CACHE_SIZE, RSA_USEFUL_TIME, true);// RSA密钥缓存

        this.videoCache = new ConcurrentLRUCache<>(this.videoCacheSize); // 缓存视频
        this.fileCache = new ConcurrentLRUCache<>(this.downloadFileCacheSize, this.downloadFileCacheExpireTime, TimeUnit.MINUTES); // 缓存文件(30分钟)
        this.customTimer = new CustomTimer();
        this.settingThreadPool = Executors.newCachedThreadPool(); // 线程池

        if (isConfigInvalid()) {
            log.error("初始化项目配置失败，请检查配置!");
            FileCloudDiskApplication.close();
            throw new InitFailedException("初始化项目配置失败，请检查配置!");
        }
        TokenUtils.init(this);
        if (loginAndRegisterUseEncrypt) {
            log.info("登入注册模块使用加密");
        } else {
            log.warn("登入注册模块未使用加密");
        }


        File shareTempFolder = new File(Setting.USER_SHARE_TEMP_DIR_PATH);
        if (shareTempFolder.exists()) {
            settingThreadPool.submit(() -> {
                log.info("正在扫描分享缓存文件夹: {}", shareTempFolder.getPath());
                ShareFile shareFile = new ShareFile();
                List<ShareFile> shareFileList = shareFileMapper.selectAll();

                for (ShareFile file : shareFileList) {
                    LocalDateTime createTime = file.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if ((createTime.plusMinutes(maximumSurvivalTimeOfSharedFile)).isBefore(LocalDateTime.now())) {
                        try {
                            log.warn("扫描时发现超过最大存活时间的分享文件,分享码为: {} ,文件路径为: {} ,已自动删除!", file.getCode(), file.getFilePath());
                            FileUtils.delete(file.getFilePath());
                            shareFileMapper.deleteByCode(file.getCode());
                            if (shareTempFolder.listFiles() == null || Objects.requireNonNull(shareTempFolder.listFiles()).length == 0) {
                                //如果文件夹是空的,则删除文件夹
                                FileUtils.delete(shareTempFolder);
                            }
                        } catch (IOException e) {
                            log.error("删除超过最大存活时间的分享文件失败: {} ,原因: {}", file, e.getMessage());
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }

    private boolean isConfigInvalid() {
        return Stream.of(
                port, applicationName, systemType, serverIP, completeServerURL, invitationCode,
                loginAndRegisterTimeCache, rasCache, maxNumberOfAttempts, signature, tokenLifeTime, videoCache, fileCache
                , videoCacheSize, downloadFileCacheSize, downloadFileCacheExpireTime, settingThreadPool, maximumSurvivalTimeOfSharedFile,
                localOperationOnly
        ).anyMatch(value -> {
            if (value == null) {
                return true;
            }
            if (value instanceof String && !StringUtils.hasLength((String) value)) {
                return true;
            }
            if (value instanceof Long && (Long) value <= 0) {
                return true;
            }
            if (value instanceof Integer && (Integer) value <= 0) {
                return true;
            }
            return false;
        });
    }

    @PreDestroy
    public void destroy() {
        log.info("正在释放缓存...");
        customTimer.shutdownNow();
        settingThreadPool.shutdownNow();
        videoCache.shutdown();
        fileCache.shutdown();
        loginAndRegisterTimeCache.shutdown();
        rasCache.shutdown();
        log.info("缓存释放完毕!");
    }

    // springboot注入
    @Resource
    private ShareFileMapper shareFileMapper;

    //完全静态变量
    public static final String TOKEN_HEADER_NAME = "Authorization"; // 真token头名称
    public static final String TOKEN_NAME_FOR_FE = "Token"; // 前端Token名称

    public static final Integer BYTE_CACHE_SIZE = 65536;// 缓存大小

    public static final String FE_USER_BASE_URL = "/user"; // 用户模块前缀
    public static final String USER_DOWNLOAD_TEMP_DIR_PATH = "./downloadTemp"; // 用户下载临时目录
    public static final String USER_SHARE_TEMP_DIR_PATH = "./shareTemp"; // 用户分享临时目录
    public static final String USER_UPLOAD_TEMP_DIR_PATH = "./uploadTemp"; // 用户上传临时目录

    public static final String USER_OPERATION_LOG_PREFIX = "用户操作日志[ {} ]"; // 用户操作日志前缀
}