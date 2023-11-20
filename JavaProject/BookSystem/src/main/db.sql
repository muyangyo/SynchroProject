-- 创建数据库
DROP DATABASE IF EXISTS book_system;
CREATE DATABASE book_system DEFAULT CHARACTER SET utf8mb4;
USE book_system;
-- 用户表
DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
 `id` INT NOT NULL AUTO_INCREMENT,
 `user_name` VARCHAR ( 128 ) NOT NULL,
 `password` VARCHAR ( 128 ) NOT NULL,
 `delete_flag` TINYINT ( 4 ) NULL DEFAULT 1,
 `create_time` DATETIME DEFAULT now(),
 `update_time` DATETIME DEFAULT now() ON UPDATE now(),PRIMARY KEY ( `id` ),
UNIQUE INDEX `user_name_UNIQUE` ( `user_name` ASC )) ENGINE = INNODB COMMENT = '用户表';
-- 图书表
DROP TABLE IF EXISTS book_info;
CREATE TABLE `book_info` (
 `id` INT ( 11 ) NOT NULL AUTO_INCREMENT,
 `book_name` VARCHAR ( 127 ) NOT NULL,
 `author` VARCHAR ( 127 ) NOT NULL,
 `count` INT ( 11 ) NOT NULL,
 `price` DECIMAL (7,2 ) NOT NULL,
 `publish` VARCHAR ( 256 ) NOT NULL,
 `status` TINYINT ( 4 ) DEFAULT 1 COMMENT '0-无效, 1-正常, 2-不允许借阅',
 `create_time` DATETIME DEFAULT now(),
 `update_time` DATETIME DEFAULT now() ON UPDATE now(),
 PRIMARY KEY ( `id` )
) ENGINE = INNODB;

-- 初始化数据
INSERT INTO user_info ( user_name, PASSWORD ) VALUES ( "admin", "admin" );
INSERT INTO user_info ( user_name, PASSWORD ) VALUES ( "zhangsan", "123456" );

-- 初始化数据
INSERT INTO book_info (book_name,author,count,price,publish,status) VALUES ("西游记","作者",3,33.00,"社区",1);
INSERT INTO book_info (book_name,author,count,price,publish,status) VALUES ("三国演义","作者",5,30.00,"社区",1);
INSERT INTO book_info (book_name,author,count,price,publish,status) VALUES ("红楼梦","作者",10,36.00,"社区",1)
