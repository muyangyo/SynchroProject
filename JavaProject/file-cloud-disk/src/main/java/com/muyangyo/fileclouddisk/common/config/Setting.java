package com.muyangyo.fileclouddisk.common.config;


import com.muyangyo.fileclouddisk.FileCloudDiskApplication;
import com.muyangyo.fileclouddisk.common.exception.InitFailedException;
import com.muyangyo.fileclouddisk.common.model.enums.SystemType;
import com.muyangyo.fileclouddisk.common.model.meta.RecycleBinFile;
import com.muyangyo.fileclouddisk.common.model.meta.ShareFile;
import com.muyangyo.fileclouddisk.common.utils.*;
import com.muyangyo.fileclouddisk.user.mapper.RecycleBinFileMapper;
import com.muyangyo.fileclouddisk.user.mapper.ShareFileMapper;
import com.muyangyo.fileclouddisk.user.service.FileService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Configuration
@DependsOn("metaDataBaseInitializer")
@Slf4j
@Getter
public class Setting {
    @Value("${server.port}")
    private Integer port;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${server.ssl.enabled:false}")
    private boolean sslEnabled; // 是否开启ssl(http/https)

    private SystemType systemType;
    private String serverIP;
    private String completePublicServerURL;// 公网服务器地址
    private String completeLocalServerURL;// 本地服务器地址
    @Value("${invitationCode}")
    private String invitationCode;
    @Value("${recycleBin}")
    private boolean useRecycleBin;

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
    private ConcurrentLRUCache<String, File> ShareFileCache; // 访客文件缓存
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
        this.completePublicServerURL = (sslEnabled ? "https://" : "http://") + serverIP + ":" + port;
        this.completeLocalServerURL = (sslEnabled ? "https://" : "http://") + "127.0.0.1" + ":" + port;
        this.loginAndRegisterTimeCache = new EasyTimedCache<>(CACHE_SIZE, LIMIT_TIME, true);// 登录和注册时间缓存
        this.rasCache = new EasyTimedCache<>(CACHE_SIZE, RSA_USEFUL_TIME, true);// RSA密钥缓存

        this.videoCache = new ConcurrentLRUCache<>(this.videoCacheSize); // 缓存视频
        this.fileCache = new ConcurrentLRUCache<>(this.downloadFileCacheSize, this.downloadFileCacheExpireTime, TimeUnit.MINUTES); // 缓存文件(30分钟)
        this.ShareFileCache = new ConcurrentLRUCache<>(3, this.downloadFileCacheExpireTime, TimeUnit.MINUTES); // 缓存访客文件(30分钟)
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


        // 初始化时扫描分享缓存文件夹,删除超过最大存活时间的分享文件
        File shareTempFolder = new File(Setting.USER_SHARE_TEMP_DIR_PATH);
        if (shareTempFolder.exists()) {
            settingThreadPool.submit(() -> {
                log.info("正在扫描分享缓存文件夹: {}", Setting.USER_SHARE_TEMP_DIR_PATH);
                ShareFile shareFile = new ShareFile();
                List<ShareFile> shareFileList = shareFileMapper.selectAll();

                for (ShareFile file : shareFileList) {
                    LocalDateTime createTime = file.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    if ((createTime.plusMinutes(maximumSurvivalTimeOfSharedFile)).isBefore(LocalDateTime.now())) {
                        try {
                            log.info("扫描时发现超过最大存活时间的分享文件,分享码为: {} ,文件路径为: {} ,已自动删除!", file.getCode(), file.getFilePath());
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
                log.info("扫描分享缓存文件夹完毕!");
            });
        }

        // 初始化时扫描回收站文件夹,删除超过7天的文件
        File recycleBinFolder = new File(Setting.USER_RECYCLE_BIN_DIR_PATH);
        if (recycleBinFolder.exists()) {
            settingThreadPool.submit(() -> {
                log.info("正在扫描回收站文件夹: {}", Setting.USER_RECYCLE_BIN_DIR_PATH);
                final List<RecycleBinFile> recycleBinFileList = recycleBinFileMapper.selectAll();
                LocalDateTime now = LocalDateTime.now();// 当前时间
                int count = 0;

                for (RecycleBinFile recycleBinFile : recycleBinFileList) {

                    LocalDateTime deleteTime = recycleBinFile.getDeleteTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();// 获取删除时间
                    Duration duration = Duration.between(deleteTime, now);
                    long daysDifference = duration.toDays(); // 以天为单位相差多少
                    if (daysDifference >= 7) {
                        // 超过 7 天，执行删除操作
                        try {
                            FileUtils.delete(FileService.getRecycleBinFileRealPath(recycleBinFile.getId(), recycleBinFile.getFileName()));
                            // 删除数据库记录
                            recycleBinFileMapper.deleteByCode(recycleBinFile.getId());
                            log.info("已删除在回收站超过7天的文件 [{}]", recycleBinFile.getFileName());
                            count++;
                        } catch (IOException e) {
                            log.error("删除超过7天的文件 [{}] 失败", recycleBinFile, e);
                        }

                    }

                }
                log.info("扫描回收站文件夹完毕! 共删除 {} 个文件", count);
            });
        }

    }

    private boolean isConfigInvalid() {
        return Stream.of(
                port, applicationName, systemType, serverIP, completePublicServerURL, invitationCode,
                loginAndRegisterTimeCache, rasCache, maxNumberOfAttempts, signature, tokenLifeTime, videoCache, fileCache, ShareFileCache
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
        ShareFileCache.shutdown();
        loginAndRegisterTimeCache.shutdown();
        rasCache.shutdown();
        log.info("缓存释放完毕!");
    }

    // springboot注入
    @Resource
    private ShareFileMapper shareFileMapper;
    @Resource
    private RecycleBinFileMapper recycleBinFileMapper;

    //完全静态变量
    public static final String TOKEN_HEADER_NAME = "Authorization"; // 真token头名称
    public static final String TOKEN_NAME_FOR_FE = "Token"; // 前端Token名称

    public static final Integer BYTE_CACHE_SIZE = 65536;// 缓存大小

    public static final String FE_USER_BASE_URL = "/user"; // 用户模块前缀
    public static final String USER_DOWNLOAD_TEMP_DIR_PATH = "./downloadTemp"; // 用户下载临时目录
    public static final String USER_SHARE_TEMP_DIR_PATH = "./shareTemp"; // 用户分享临时目录
    public static final String USER_UPLOAD_TEMP_DIR_PATH = "./uploadTemp"; // 用户上传临时目录
    public static final String USER_RECYCLE_BIN_DIR_PATH = "./recycleBin"; // 用户回收站目录
    public static final String MANAGER_RELATIVE_PATH = "/#/manager"; // 管理页面相对路径
    public static final String USER_RELATIVE_PATH = "/#/user"; // 用户页面相对路径
}