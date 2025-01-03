CREATE DATABASE IF NOT EXISTS miao_miao_wu;

USE miao_miao_wu;

CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL AUTO_INCREMENT,
    openid VARCHAR(50) NOT NULL,
    session_key VARCHAR(100),
    nickname VARCHAR(50),
    avatar VARCHAR(255),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    latest_login_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    status INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS access_token (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    token VARCHAR(255),
    set_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_level (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    experience INT NOT NULL DEFAULT 0,
    ikun_level INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS drama (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    drama_name VARCHAR(255),
    cover_url VARCHAR(255),
    introduction TEXT,
    made_company VARCHAR(255),
    playing_platform VARCHAR(255) DEFAULT '2',
    is_update INT NOT NULL,
    total_number INT NOT NULL DEFAULT 0,
    update_number INT NOT NULL DEFAULT 0,
    set_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    update_time VARCHAR(255),
    love FLOAT NOT NULL DEFAULT 0,
    remark TEXT,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
