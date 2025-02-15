package com.example.syncdemo2502131.server.FileProcessingCore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/15
 * Time: 18:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TtpUser {
    private String username;
    private String password;
    private String path;
}
