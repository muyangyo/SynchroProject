-- 创建数据库
CREATE DATABASE IF NOT EXISTS blog_spring CHARSET utf8mb4;

-- 删除已存在的用户表
DROP TABLE IF EXISTS blog_spring.user;

-- 创建用户表
CREATE TABLE blog_spring.user (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    github_url VARCHAR(128) NULL,
    delete_flag TINYINT(4) NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=INNODB DEFAULT CHARSET = utf8mb4;

-- 删除已存在的博客表
DROP TABLE IF EXISTS blog_spring.blog;

-- 创建博客表
CREATE TABLE blog_spring.blog (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NULL,
    content TEXT NULL,
    user_id INT(11) NULL,
    delete_flag TINYINT(4) NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=INNODB DEFAULT CHARSET = utf8mb4;

-- 删除已存在的评论表
DROP TABLE IF EXISTS blog_spring.comment;

-- 评论表
CREATE TABLE blog_spring.comment (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    user_name VARCHAR(128) NOT NULL, -- 评论者名字
    content VARCHAR(128) NULL, -- 评论本身
    blog_id INT NOT NULL, -- 评论文章id
    delete_flag TINYINT(4) NULL DEFAULT 0,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

insert into user (user_name,password) values ("admin","admin");
insert into blog (title,content,user_id) values ("第一篇文章","测试文章",1);