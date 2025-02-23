package com.muyangyo.filesyncclouddisk.manager.service;

import cn.hutool.core.util.RandomUtil;
import com.muyangyo.filesyncclouddisk.common.config.Setting;
import com.muyangyo.filesyncclouddisk.common.initializer.SyncModuleInitializer;
import com.muyangyo.filesyncclouddisk.common.model.meta.SyncInfo;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.model.vo.SyncInfoOfClientVO;
import com.muyangyo.filesyncclouddisk.common.model.vo.SyncInfoOfServerVO;
import com.muyangyo.filesyncclouddisk.common.utils.EasyTimer;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncInfoMapper;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncLogMapper;
import converter.MetaToVoConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        if (Pattern.compile("^[a-zA-Z0-9]{3,10}$").matcher(syncName).matches()) {
            return Result.fail("同步名称不符合要求!");
        }

        if (setting.isSyncServer()) {
            // 服务器端模式

            // 1.检查本地目录是否存在
            if (!Files.exists(Paths.get(localAddress))) {
                return Result.fail("该文件夹不存在!");
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

    public Result addSyncInfoByKey(String key) {
        return null;
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
                convert.setLastSyncTime(EasyTimer.getFormatTime(syncInfo.getLastSyncTime()));
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
            return Result.success("成功切换到服务端模式");
        } else if (changeTo.equalsIgnoreCase(Setting.CLIENT)) {
            clearSyncInfo();// 先清空配置
            setting.setSyncServer(false);
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
}
