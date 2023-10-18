create database blogSystem;
use blogSystem;

-- 建users表

create table users (
    userId int primary key auto_increment,
    userName varchar(20),
    password varchar(20),
    userGitHub varchar(50) default null
);
insert into users values (null,"root","root",null),(null,"小白","12345",null);

-- 建blogs表

create table blogs (
    blogId int primary key auto_increment,
    userId int,
    title varchar(50),
    pDate datetime,
    content varchar(300)
);

insert into blogs values (null,1,"管理员测试文章1",now(),"从今天起开始敲代码....(限制字数为300)");
insert into blogs values (null,1,"管理员测试文章2",now(),"从昨天起开始敲代码....(限制字数为300)");



