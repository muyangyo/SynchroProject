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
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);