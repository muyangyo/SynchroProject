-- 创建数据库
CREATE DATABASE IF NOT EXISTS global_template CHARSET utf8mb4;
USE global_template;

-- 删除已存在的用户表
DROP TABLE IF EXISTS users;

-- 创建用户表
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    gender INT default 2,
    role INT default 2, -- 0 为root 1为普通用户 2为游客模式
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);