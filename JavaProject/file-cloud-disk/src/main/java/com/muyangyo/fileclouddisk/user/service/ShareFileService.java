package com.muyangyo.fileclouddisk.user.service;

import cn.hutool.core.util.RandomUtil;
import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.exception.OperationWithoutPermission;
import com.muyangyo.fileclouddisk.common.model.meta.ShareFile;
import com.muyangyo.fileclouddisk.common.utils.FileUtils;
import com.muyangyo.fileclouddisk.user.mapper.ShareFileMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：分享文件服务
 * User: 沐阳Yo
 * Date: 2024/12/1
 * Time: 15:10
 */
@Service
@Slf4j
public class ShareFileService {

    @Resource
    private ShareFileMapper shareFileMapper;

    @Resource
    private Setting setting;

    /**
     * 根据分享码获取分享文件信息（访客用）
     *
     * @param code 分享码
     * @return 分享文件信息
     */
    public ShareFile getSingleShareFileByCode(String code) {
        ShareFile shareFile = shareFileMapper.selectByCode(code);
        if (shareFile != null) {
            checkAndUpdateExpiredStatus(shareFile);
        }
        return shareFile;
    }

    /**
     * 根据用户名获取分享文件列表（登录用户用）
     *
     * @param username 用户名
     * @return 分享文件列表
     */
    public List<ShareFile> getShareFileListByUsername(String username) {
        ShareFile shareFile = new ShareFile();
        shareFile.setCreator(username);

        List<ShareFile> shareFiles = shareFileMapper.selectByDynamicCondition(shareFile);
        for (ShareFile file : shareFiles) {
            checkAndUpdateExpiredStatus(file);
        }
        return shareFiles;
    }

    /**
     * 删除分享链接
     *
     * @param code 分享码
     */
    public void deleteShareFileByCode(String code, String username) {
        ShareFile shareFile = shareFileMapper.selectByCode(code);
        if (shareFile.getCreator().equals(username)) {
            shareFileMapper.deleteByCode(code);
            setting.getSettingThreadPool().submit(() -> {
                try {
                    FileUtils.delete(Setting.USER_SHARE_TEMP_DIR_PATH + "/" + code);
                } catch (IOException e) {
                    log.error("删除分享临时文件夹失败", e);
                    throw new RuntimeException(e);
                }
            });
        } else {
            log.error("非法操作，非创建者无法删除分享文件 {} ", shareFile);
            throw new OperationWithoutPermission("非法操作，非创建者无法删除分享文件");
        }
    }

    /**
     * 创建分享文件
     *
     * @param path     文件路径
     * @param username 用户名
     * @return 分享码
     */
    @SneakyThrows
    public String createShareFile(String path, String username) {
        // 无论是文件还是文件夹都可以分享
        String shareCode = RandomUtil.randomString(10);
        String dirPath = Setting.USER_SHARE_TEMP_DIR_PATH + "/" + shareCode;
        FileUtils.createHiddenDirectory(dirPath);


        // 异步复制(因为对方没那么快)
        setting.getSettingThreadPool().submit(() -> {
            // 复制文件到分享临时文件夹
            try {
                FileUtils.copy(path, dirPath);
            } catch (IOException e) {
                log.error("复制文件到分享临时文件夹失败", e);
                throw new RuntimeException(e);
            }
        });


        // 保存到数据库
        ShareFile shareFile = new ShareFile(shareCode, dirPath, username, new Date(), 1);
        shareFileMapper.insertByDynamicCondition(shareFile);

        return shareCode;
    }

    /**
     * 检查并更新过期状态
     *
     * @param shareFile 分享文件
     */
    private void checkAndUpdateExpiredStatus(ShareFile shareFile) {
        LocalDate createTime = LocalDate.parse(shareFile.getCreateTime().toString());
        if (LocalDate.now().isAfter(createTime.plusDays(1))) {
            shareFile.setStatus(0); // 过期
            log.info("shareFile:{} 已过期", shareFile);
            // 异步更新数据库和删除分享临时文件夹
            setting.getSettingThreadPool().submit(() -> {
                shareFileMapper.update(shareFile);
                try {
                    FileUtils.delete(Setting.USER_SHARE_TEMP_DIR_PATH + "/" + shareFile.getCode());
                } catch (IOException e) {
                    log.error("删除分享临时文件夹失败", e);
                    throw new RuntimeException(e);
                }
            });
        }
    }
}