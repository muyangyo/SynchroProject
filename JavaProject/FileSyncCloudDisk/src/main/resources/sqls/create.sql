CREATE DATABASE `file_sync_cloud_disk` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

CREATE TABLE `users`
(
    `user_id`         varchar(15) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
    `username`        varchar(30) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '昵称',
    `password`        varchar(64) COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '密码',
    `create_time`     datetime                                        DEFAULT NULL COMMENT '创建时间',
    `last_login_time` datetime                                        DEFAULT NULL COMMENT '上次登入时间',
    `status`          tinyint(1)                                      DEFAULT NULL COMMENT '0: 禁用 1:启用',
    `use_space`       bigint                                          DEFAULT NULL COMMENT '空间使用大小',
    `total_space`     bigint                                          DEFAULT NULL COMMENT '总空间大小',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `key_username` (`username`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

