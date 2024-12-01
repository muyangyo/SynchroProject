package com.muyangyo.fileclouddisk.user.service;

import com.muyangyo.fileclouddisk.common.config.Setting;
import com.muyangyo.fileclouddisk.common.model.meta.ShareFile;
import com.muyangyo.fileclouddisk.user.mapper.ShareFileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
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
        ShareFile shareFile = shareFileMapper.selectById(code);
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
    public void deleteShareFileByCode(String code) {
        shareFileMapper.deleteById(code);
    }

    /**
     * 创建分享文件
     *
     * @param path     文件路径
     * @param username 用户名
     * @return 分享码
     */
    public String createShareFile(String path, String username) {
        // TODO: 实现创建分享文件的逻辑
        return null;
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
            // 异步更新数据库
            setting.getSettingThreadPool().submit(() -> {
                shareFileMapper.update(shareFile);
            });
        }
    }
}