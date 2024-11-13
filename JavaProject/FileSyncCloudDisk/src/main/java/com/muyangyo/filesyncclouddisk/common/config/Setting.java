package com.muyangyo.filesyncclouddisk.common.config;

import com.muyangyo.filesyncclouddisk.FileSyncCloudDiskApplication;
import com.muyangyo.filesyncclouddisk.common.exception.InitFailedException;
import com.muyangyo.filesyncclouddisk.common.model.enumeration.SystemType;
import com.muyangyo.filesyncclouddisk.common.utils.NetworkUtils;
import com.muyangyo.filesyncclouddisk.common.utils.OSUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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


    @PostConstruct
    public void init() {
        this.systemType = OSUtils.getSystemType();
        this.localIp = NetworkUtils.getServerIP();
        this.absoluteUrl = "http://" + localIp + ":" + port;

        boolean isConfigInvalid = Stream.of(port, applicationName, systemType, localIp, absoluteUrl, invitationCode)
                .anyMatch(value -> value == null || (value instanceof String && !StringUtils.hasLength((String) value)));

        if (isConfigInvalid) {
            log.error("初始化项目配置失 败，请检查配置!");
            FileSyncCloudDiskApplication.close();
            throw new InitFailedException("初始化项目配置失败，请检查配置!");
        }
    }
}