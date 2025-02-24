package com.muyangyo.filesyncclouddisk.manager.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.exception.IllegalLoginWithoutRSA;
import com.muyangyo.filesyncclouddisk.common.initializer.SyncModuleInitializer;
import com.muyangyo.filesyncclouddisk.common.model.dto.SyncDTO;
import com.muyangyo.filesyncclouddisk.common.model.meta.SyncInfo;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.model.vo.SyncInfoOfClientVO;
import com.muyangyo.filesyncclouddisk.common.model.vo.SyncInfoOfServerVO;
import com.muyangyo.filesyncclouddisk.common.model.vo.SyncStatusClientOnlyVO;
import com.muyangyo.filesyncclouddisk.common.utils.EasyTimedCache;
import com.muyangyo.filesyncclouddisk.common.utils.EasyTimer;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncInfoMapper;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncLogMapper;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncStatus;
import converter.MetaToVoConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/23
 * Time: 18:57
 */
@Service
@Slf4j
public class SyncInfoService {
    @Resource
    private SyncInfoMapper syncInfoMapper;
    @Resource
    private Setting setting;
    @Resource
    private SyncLogMapper syncLogMapper;
    @Resource
    private SyncModuleInitializer syncModuleInitializer;

    @SneakyThrows
    public Result addSyncInfo(String syncName, String localAddress) {
        if (!Pattern.compile("^[a-zA-Z0-9]{3,10}$").matcher(syncName).matches()) {
            return Result.fail("同步名称不符合要求!"); // 限制syncName的长度为3-10位，且只能包含字母和数字
        }

        if (setting.isSyncServer()) {
            // 服务器端模式

            // 1.检查本地目录是否存在
            Path tmp = Paths.get(localAddress);
            if (!Files.exists(tmp) || !Files.isDirectory(tmp)) {
                return Result.fail("路径非法!必须为文件夹!"); // 如果不存在或者不是文件夹，则返回失败
            }

            //2.检查是否已存在同名同步
            SyncInfo syncInfo = new SyncInfo();
            syncInfo.setUsername(syncName);
            if (!syncInfoMapper.selectByCondition(syncInfo).isEmpty()) {
                return Result.fail("已存在同名同步!");
            }

            //3.检查是否有同本地目录的同步
            SyncInfo temp = new SyncInfo();
            temp.setLocalAddress(localAddress);
            if (!syncInfoMapper.selectByCondition(temp).isEmpty()) {
                return Result.fail("已存在相同本地目录的同步!");
            }
            syncInfo.setLocalAddress(localAddress);
            String password = RandomUtil.randomString(6).toLowerCase();
            syncInfo.setPassword(password);

            // 4.插入同步信息
            syncInfoMapper.insert(syncInfo);
            log.info("同步信息添加成功! 类型 [server] , 同步名称 [{}] , 本地目录 [{}] , 密码 [{}]", syncName, localAddress, password);

            //5.重新启动服务
            if (syncModuleInitializer.restartServer()) {
                return Result.success("成功加载新的同步配置!");
            }
        }
        return Result.fail("非法的操作!");
    }

    public Result addSyncInfoByKey(String key, String formLocalPath) {
        if (setting.isSyncServer()) {
            // 如果是服务端模式，则不允许添加
            return Result.fail("非法操作!非客户端模式!");
        }
        SyncInfo syncInfoByShareLink = getSyncInfoByShareLink(key, formLocalPath);

        if (syncInfoMapper.selectByName(syncInfoByShareLink.getUsername()) != null) {
            return Result.fail("同步已存在!");
        }

        syncInfoMapper.insert(syncInfoByShareLink);
        log.info("同步信息添加成功! 类型 [client] , 同步名称 [{}] , 本地目录 [{}] , 服务器IP [{}] , 设备ID [{}]", syncInfoByShareLink.getUsername(), syncInfoByShareLink.getLocalAddress(),
                syncInfoByShareLink.getServerIp(), setting.getDeviceID()
        );
        if (syncModuleInitializer.restartClient()) {
            return Result.success("同步信息添加成功!");
        }
        return Result.fail("同步信息添加失败!");
    }

    @SneakyThrows
    public Result getSyncInfoList() {
        if (setting.isSyncServer()) {
            // 服务器端模式
            List<SyncInfo> syncInfos = syncInfoMapper.selectAll();
            ArrayList<SyncInfoOfServerVO> ret = new ArrayList<>(syncInfos.size());
            for (SyncInfo syncInfo : syncInfos) {
                SyncInfoOfServerVO convert = MetaToVoConverter.convert(syncInfo, SyncInfoOfServerVO.class);
                convert.setLocalSize(FileUtils.getFolderSize(Paths.get(syncInfo.getLocalAddress()))); // 获取本地目录大小
                convert.setVersionDelete(setting.isVersionDelete()); // 获取是否开启版本删除功能
                ret.add(convert);
            }
            return Result.success(ret);
        } else {
            // 客户端模式
            List<SyncInfo> syncInfos = syncInfoMapper.selectAll();
            ArrayList<SyncInfoOfClientVO> ret = new ArrayList<>(syncInfos.size());
            for (SyncInfo syncInfo : syncInfos) {
                SyncInfoOfClientVO convert = MetaToVoConverter.convert(syncInfo, SyncInfoOfClientVO.class);
                convert.setLocalSize(FileUtils.getFolderSize(Paths.get(syncInfo.getLocalAddress()))); // 获取本地目录大小
                Date lastSyncTime = syncInfo.getLastSyncTime();
                if (lastSyncTime == null) {
                    convert.setLastSyncTime("未初始化!");
                } else {
                    convert.setLastSyncTime(EasyTimer.getFormatTime(syncInfo.getLastSyncTime()));
                }
                ret.add(convert);
            }
            return Result.success(ret);
        }
    }

    public Result getSyncStatus() {
        if (setting.isSyncServer()) {
            return Result.success("server");
        } else {
            return Result.success("client");
        }
    }

    public Result changeSyncStatus(String changeTo) {
        if (changeTo.equalsIgnoreCase(Setting.SERVER)) {
            clearSyncInfo();// 先清空配置
            setting.setSyncServer(true);
            syncModuleInitializer.stopServer();
            log.info("用户切换到服务端模式");
            return Result.success("成功切换到服务端模式");
        } else if (changeTo.equalsIgnoreCase(Setting.CLIENT)) {
            clearSyncInfo();// 先清空配置
            setting.setSyncServer(false);
            syncModuleInitializer.stopServer();
            log.info("用户切换到客户端模式");
            return Result.success("成功切换到客户端模式");
        } else {
            return Result.fail("切换失败，未知模式");
        }
    }

    private void clearSyncInfo() {
        // 1.清空配置
        syncInfoMapper.deleteAll();
        // 2.清空日志
        syncLogMapper.deleteAll();
    }

    @SneakyThrows
    public Result updateSyncInfoOfServer(String syncName, String localPath) {
        if (!setting.isSyncServer()) {
            return Result.fail("非法操作!非服务端模式!");
        }

        Path tmp = Paths.get(localPath);
        if (!Files.exists(tmp) || !Files.isDirectory(tmp)) {
            return Result.fail("路径非法!必须为文件夹!");
        }

        SyncInfo syncInfo = syncInfoMapper.selectByName(syncName);
        if (syncInfo == null) {
            return Result.fail("同步信息不存在!");
        }

        String oldLocalPath = syncInfo.getLocalAddress();
        if (oldLocalPath.equals(localPath)) {
            return Result.fail("同步信息未修改!");
        }

        syncInfo.setLocalAddress(localPath);
        syncInfoMapper.updateByName(syncInfo);
        log.info("同步信息更新成功! 类型 [{}] , 同步名称 [{}] , 本地目录 [{}] => [{}]", "server",
                syncName, oldLocalPath, localPath);
        if (syncModuleInitializer.restartServer()) {
            return Result.success("同步信息更新成功!");
        }
        return Result.fail("同步信息更新失败!");
    }

    public Result getSyncShareLink(String syncName, String key) {
        if (!setting.isSyncServer()) {
            return Result.fail("非法操作!");
        }
        // 检查同步信息是否存在
        SyncInfo syncInfo = syncInfoMapper.selectByName(syncName);
        if (syncInfo == null) {
            return Result.fail("同步信息不存在!");
        }

        String link = generateShareLink(syncInfo);
        RSA rsa = new RSA(null, key);
        return Result.success(rsa.encryptBase64(link, KeyType.PublicKey));
    }

    private String generateShareLink(SyncInfo syncInfo) {
        //格式为 ip@sn=syncName&pw=password&sid=serverId
        return setting.getServerIP() + "@sn=" + syncInfo.getUsername() + "&pw=" + syncInfo.getPassword() + "&sid=" + setting.getDeviceID();
    }

    private SyncInfo getSyncInfoByShareLink(String link, String localAddress) {
        // 使用正则表达式拆解字符串
        String[] parts = link.split("[@=&]");

        // 提取所需的字段
        String ip = parts[0];
        String syncName = parts[2];
        String password = parts[4];
        String serverId = parts[6];

        //创建SyncInfo对象
        return new SyncInfo(syncName, password, localAddress, true, serverId, ip, true, null);
    }

    @SneakyThrows
    public Result deleteSyncInfo(String syncName) {
        SyncInfo syncInfo = syncInfoMapper.selectByName(syncName);
        if (syncInfo == null) {
            return Result.fail("同步信息不存在!");
        }

        syncInfoMapper.deleteByName(syncName);
        log.info("同步信息删除成功! 类型 [{}] , 同步名称 [{}] , 本地目录 [{}]", setting.isSyncServer() ? "server" : "client",
                syncName, syncInfo.getLocalAddress());

        boolean isZero = syncInfoMapper.getCount() == 0;
        if (setting.isSyncServer()) {
            // 如果删除了最后一个同步信息，则停止服务
            if (isZero) {
                syncModuleInitializer.stopServer();
                log.info("目前没有同步信息，已停止服务");
                return Result.success("同步信息删除成功!");
            } else {
                // 其他情况，重新启动服务
                if (syncModuleInitializer.restartServer()) {
                    return Result.success("同步信息删除成功!");
                }
            }
        } else {
            if (isZero) {
                // 如果删除了最后一个同步信息，则停止客户端
                syncModuleInitializer.stopClient();
                log.info("目前没有同步信息，已停止客户端");
                return Result.success("同步信息删除成功!");
            } else {
                // 其他情况，重新启动客户端
                if (syncModuleInitializer.restartClient()) {
                    return Result.success("同步信息删除成功!");
                }
            }
        }
        return Result.fail("同步信息删除失败!");
    }

    public Result getPublicKey(String ip) {
        RSA rsa = SecureUtil.rsa();
        EasyTimedCache<String, PrivateKey> rasCache = setting.getRasCache();
        rasCache.put(ip, rsa.getPrivateKey()); // 缓存私钥

        String publicKey = rsa.getPublicKeyBase64(); // 发送公钥给客户端
        return Result.success(publicKey);
    }

    public void decryptSyncDTO(SyncDTO syncDTO, String ip) {
        EasyTimedCache<String, PrivateKey> rasCache = setting.getRasCache();
        PrivateKey privateKey = rasCache.get(ip); // 根据IP获取私钥

        RSA rsa = SecureUtil.rsa();
        rsa.setPrivateKey(privateKey);
        try {
            syncDTO.setLocalPath(rsa.decryptStr(syncDTO.getLocalPath(), KeyType.PrivateKey));
            syncDTO.setKey(rsa.decryptStr(syncDTO.getKey(), KeyType.PrivateKey));
        } catch (Exception e) {
            throw new IllegalLoginWithoutRSA("解密新同步信息失败!请检查私钥是否正确!");
        }
        log.info("解密同步信息成功!");
    }

    @SneakyThrows
    public List<SyncStatusClientOnlyVO> getSyncStatusClientOnly(List<String> syncNames) {
        if (setting.isSyncServer()) {
            throw new IllegalAccessException("非法操作!非客户端模式!");
        }
        ArrayList<SyncStatusClientOnlyVO> ret = new ArrayList<>(syncNames.size());

        List<SyncInfo> syncInfos = syncInfoMapper.selectAll();
        SyncInfo defaultSyncInfo = new SyncInfo();
        defaultSyncInfo.setServerIp("");
        defaultSyncInfo.setLocalAddress("");
        for (String syncName : syncNames) {
            SyncStatus syncStatuses = syncModuleInitializer.getSyncStatuses(
                    syncName,
                    syncInfos.stream().filter(s -> s.getUsername().equals(syncName)).findFirst().orElse(defaultSyncInfo).getServerIp(),
                    syncInfos.stream().filter(s -> s.getUsername().equals(syncName)).findFirst().orElse(defaultSyncInfo).getLocalAddress()
            );
            SyncStatusClientOnlyVO vo = new SyncStatusClientOnlyVO(syncName, syncStatuses);
            ret.add(vo);
        }
        return ret;
    }

    public Result updateSyncInfoOfClient(String syncName, String localPath, String ip) {
        if (setting.isSyncServer()) {
            return Result.fail("非法操作!非客户端模式!");
        }

        // 1. 检查本地目录是否存在
        Path tmp = Paths.get(localPath);
        if (!Files.exists(tmp) || !Files.isDirectory(tmp)) {
            return Result.fail("路径非法!必须为文件夹!");
        }

        // 2. 检查同步信息是否存在
        SyncInfo syncInfo = syncInfoMapper.selectByName(syncName);
        if (syncInfo == null) {
            return Result.fail("同步信息不存在!");
        }

        // 3. 检查是否需要更新
        String oldLocalPath = syncInfo.getLocalAddress();
        String oldServerIp = syncInfo.getServerIp();
        if (oldLocalPath.equals(localPath) && oldServerIp.equals(ip)) {
            return Result.fail("同步信息未修改!");
        }

        // 4. 更新同步信息
        syncInfo.setLocalAddress(localPath);
        syncInfo.setServerIp(ip);
        syncInfoMapper.updateByName(syncInfo);
        log.info("同步信息更新成功! 类型 [{}] , 同步名称 [{}] , 本地目录 [{}] => [{}], 服务器IP [{}] => [{}]", "client",
                syncName, oldLocalPath, localPath, oldServerIp, ip);
        if (syncModuleInitializer.restartClient()) {
            return Result.success("同步信息更新成功!");
        }
        return Result.fail("同步信息更新失败!");
    }


    public Result pauseOrStartSync(String newStatus, String syncName) {
        if (setting.isSyncServer()) {
            return Result.fail("非法操作!非客户端模式!");
        }
        boolean newStatusBoolean;
        if (newStatus.equalsIgnoreCase("SYNC_STOP")) {
            newStatusBoolean = false;
        } else if (newStatus.equalsIgnoreCase("SYNCING")) {
            newStatusBoolean = true;
        } else {
            return Result.fail("非法操作!");
        }

        // 1. 检查同步信息是否存在
        SyncInfo syncInfo = syncInfoMapper.selectByName(syncName);
        if (syncInfo == null) {
            return Result.fail("同步信息不存在!");
        }

        // 2. 检查是否需要更新
        boolean oldStatus = syncInfo.isActive();
        if (oldStatus == newStatusBoolean) {
            return Result.fail("同步状态未修改!");
        }

        //3. 更新同步信息
        syncInfo.setActive(newStatusBoolean);
        syncInfoMapper.updateByName(syncInfo);
        log.info("同步状态更新成功! 类型 [{}] , 同步名称 [{}] , 活跃状态 [{}] => [{}]", "client",
                syncName, oldStatus, newStatusBoolean);
        if (syncModuleInitializer.restartClient()) {
            return Result.success("同步状态更新成功!");
        }
        return Result.fail("同步状态更新失败!");
    }
}



