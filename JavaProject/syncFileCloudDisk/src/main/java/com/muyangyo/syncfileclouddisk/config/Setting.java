package com.muyangyo.syncfileclouddisk.config;

import com.muyangyo.syncfileclouddisk.SyncFileCloudDiskApplication;
import com.muyangyo.syncfileclouddisk.exceptions.InitFailedException;
import com.muyangyo.syncfileclouddisk.model.enumeration.SystemType;
import com.muyangyo.syncfileclouddisk.utils.NetworkUtils;
import com.muyangyo.syncfileclouddisk.utils.OSUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

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


    @PostConstruct
    public void init() {
        this.systemType = OSUtils.getSystemType();
        this.localIp = NetworkUtils.getServerIP();
        this.absoluteUrl = "http://" + localIp + ":" + port;

        if (!StringUtils.hasLength(localIp) || systemType == null || port == null || absoluteUrl == null) {
            log.error("初始化项目配置失败，请检查配置!");
            SyncFileCloudDiskApplication.close();
            throw new InitFailedException("初始化项目配置失败，请检查配置!");
        }
    }

}