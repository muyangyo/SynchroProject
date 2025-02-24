package com.muyangyo.filesyncclouddisk.common.initializer;

import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.model.meta.SyncInfo;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncInfoMapper;
import com.muyangyo.filesyncclouddisk.manager.service.SyncLogService;
import com.muyangyo.filesyncclouddisk.syncCore.client.fileSyncProcessingCore.FileSyncManager;
import com.muyangyo.filesyncclouddisk.syncCore.client.fileSyncProcessingCore.FileSyncOneShotProcessor;
import com.muyangyo.filesyncclouddisk.syncCore.client.linkCore.DeviceExplorer;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.FtpLoginUser;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncStatus;
import com.muyangyo.filesyncclouddisk.syncCore.server.fileSyncProcessingCore.FTPsServer;
import com.muyangyo.filesyncclouddisk.syncCore.server.linkCore.DiscoveryServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.FtpServer;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.muyangyo.filesyncclouddisk.common.utils.SSLUtils.autoGenerateKeystore;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/21
 * Time: 10:42
 */
@Component
@DependsOn({"metaDataBaseInitializer", "setting"})
@Slf4j
public class SyncModuleInitializer {
    @Resource
    private Setting setting;
    @Resource
    private SyncLogService syncLogService; // 同步 日志 数据库操作接口
    @Resource
    private SyncInfoMapper syncInfoMapper; // 同步 配置 数据库操作接口
    // 用于保存服务实例
    private DiscoveryServer discoveryServer;
    private FtpServer ftpServer;
    private final Map<String, FileSyncManager> mapForFileSyncManagers = new HashMap<>(); // key: serverIp, value: FileSyncManager

    @PostConstruct
    public void init() {
        //通过数据库来判断是否需要启动服务
        List<SyncInfo> syncInfos = syncInfoMapper.selectAll();
        if (syncInfos.isEmpty()) {
            log.info("未发现任何同步配置，不启动同步服务");
            return;
        } else {
            SyncInfo syncInfo = syncInfos.get(0);//随便取一个,来判断是否是服务端还是客户端
            if (StringUtils.hasLength(syncInfo.getServerId()) || StringUtils.hasLength(syncInfo.getServerIp())) {
                log.info("判断当前为 [客户端]");
                log.info("启动同步客户端");
                startClient();
            } else {
                log.info("判断当前为 [服务端]");
                log.info("启动同步服务端");
                startServer();
            }
        }
    }

    public boolean startServer() {
        stopClient(); // 先停止客户端,避免端口冲突
        setting.setSyncServer(true);
        // 启动UDP广播监听
        discoveryServer = new DiscoveryServer(setting.getDiscoveryServicePort(), setting.getDeviceID());
        setting.getSettingThreadPool().submit(discoveryServer);

        // 启动FTPS Server前先检查下有没有证书
        if (!Files.exists(Paths.get("./" + setting.getServerCertificateName()))) {
            // 自动生成 keystore.jks 文件
            log.info("{} 证书文件不存在正在自动生成...", setting.getServerCertificateName());
            if (autoGenerateKeystore(setting.getServerCertificatePassword(), setting.getServerCertificateName())) {
                log.info("{} 证书文件生成成功", setting.getServerCertificateName());
            } else {
                log.error("{} 证书文件生成失败", setting.getServerCertificateName());
                return false;
            }
        }

        List<SyncInfo> syncInfos = syncInfoMapper.selectAll();
        ArrayList<FtpLoginUser> ftpLoginUsers = new ArrayList<>(syncInfos.size());
        for (SyncInfo syncInfo : syncInfos) {
            FtpLoginUser ftpLoginUser = new FtpLoginUser(syncInfo.getUsername(), syncInfo.getPassword(), syncInfo.getLocalAddress());
            ftpLoginUsers.add(ftpLoginUser);
        }

        // 启动FTPS Server
        try {
            ftpServer = FTPsServer.run(setting.getServerCertificateName(), setting.getServerCertificatePassword(),
                    null, setting.getFtpsPort(), ftpLoginUsers, setting.isVersionDelete(),
                    setting.getVersionDeleteMaxCount(), setting.getVersionDeleteName(), syncLogService);
        } catch (Exception e) {
            log.error("FTPS服务器启动失败！", e);
            stopServer();
            return false;
        }

        return true;
    }

    @PreDestroy
    public boolean stopServer() {
        log.info("正在清空同步服务端资源");
        if (discoveryServer != null) {
            discoveryServer.stop();
            discoveryServer = null;
            log.info("UDP广播服务已停止");
        }
        if (ftpServer != null && !ftpServer.isStopped()) {
            ftpServer.stop();
            ftpServer = null;
            log.info("FTPS服务器已停止");
        }
        return true;
    }

    public boolean restartServer() throws InterruptedException {
        log.warn("正在重启同步服务端");
        stopServer();
        return startServer();
    }

    @PreDestroy
    public boolean stopClient() {
        log.info("正在清空同步客户端资源");
        mapForFileSyncManagers.forEach((serverIp, fileSyncManager) -> {
            fileSyncManager.stopAllSyncs(); // 停止所有同步任务
        });
        mapForFileSyncManagers.clear(); // 清理资源
        return true;
    }


    public boolean restartClient() {
        log.warn("正在重启同步客户端");
        stopClient();
        return startClient();
    }

    public boolean startClient() {
        stopServer(); // 先停止服务端,避免端口冲突
        setting.setSyncServer(false);
        List<SyncInfo> syncs = syncInfoMapper.selectActive();// 只有激活的同步配置才会被执行


        // 根据 serverIp 进行分组
        Map<String, List<SyncInfo>> groupByServerIp = syncs.stream().collect(Collectors.groupingBy(SyncInfo::getServerIp));
        groupByServerIp.forEach((serverIp, syncInfoList) -> {
            // 以 serverIp 作为 key ,取出 serverIp 对应的 syncInfoList

            setting.getSettingThreadPool().submit(() -> {
                //获取本次实际的IP
                String realServerIp = DeviceExplorer.connectToServer(serverIp, setting.getDiscoveryServicePort(),
                        syncInfoList.get(0).getServerId()); // 这里随便取一个syncInfo,因为同一个ServerIP 的 serverId 是一样的
                if (StringUtils.hasLength(realServerIp)) {
                    log.info("成功连接到服务器 [{}] ,执行文件同步操作", realServerIp);
                    FileSyncManager fileSyncManager = new FileSyncManager(realServerIp, setting.getFtpsPort(),
                            syncInfoList, setting.getSettingThreadPool(), syncInfoMapper, syncLogService);
                    mapForFileSyncManagers.put(serverIp, fileSyncManager); // 保存实例
                } else {
                    log.error("连接服务器 [{}] 失败！无法执行文件同步", serverIp);
                }
            });

        });
        return true;
    }

    // 新增方法获取同步状态
    public SyncStatus getSyncStatuses(String syncName, String serverIp, String localAddress) {
        // 先看看是不是激活的,没激活的不会在内存中
        List<SyncInfo> syncInfos = syncInfoMapper.selectActive();
        SyncInfo syncInfo = syncInfos.stream().filter(s -> s.getUsername().equals(syncName)).findFirst().orElse(null);
        if (syncInfo == null) {
            return SyncStatus.SYNC_STOP;
        }

        FileSyncManager fileSyncManager = mapForFileSyncManagers.get(serverIp);
        if (fileSyncManager != null) {
            FileSyncOneShotProcessor fileSyncOneShotProcessor = fileSyncManager.CURRENT_SYNCS.get(localAddress);
            if (fileSyncOneShotProcessor != null) {
                return fileSyncOneShotProcessor.getSyncStatus();
            }
        }
        return SyncStatus.WAIT_CONNECT;//默认返回的是等待连接状态
    }
}
