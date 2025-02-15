import cn.hutool.extra.ftp.SimpleFtpServer;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.util.ArrayList;

public class SimpleFtpServerDemo {
    public static void main(String[] args) {
        ArrayList<Authority> authorities = new ArrayList<>();
        authorities.add(new WritePermission());// 添加写权限

        BaseUser user = new BaseUser();
        user.setName("123");
        user.setPassword("123");
        user.setHomeDirectory("C:\\Users\\MuYang\\Desktop\\前端Vue精品课课件");
        user.setEnabled(true);
        user.setMaxIdleTime(300);
        user.setAuthorities(authorities);

        File sslFile = new File("./keystore.jks");
        if (!sslFile.exists()) {
            System.out.println("keystore.jks 文件不存在");
            return;
        }

        SimpleFtpServer
                .create()
                .setSsl(sslFile, "123456") // 配置 SSL/TLS
                .setPort(21)
                .addUser(user)
                .start();
    }

/*    public static void main(String[] args) throws Exception {
        // 1. 创建 FTP 服务器工厂
        FtpServerFactory serverFactory = new FtpServerFactory();

        // 2. 配置监听器（设置端口）
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(2121); // 默认 FTP 端口是 21，此处示例使用 2121
        serverFactory.addListener("default", listenerFactory.createListener()); // 设置默认监听器

        // 3. 配置用户管理器（基于内存的简单用户管理）
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        userManagerFactory.setFile(null); // 不使用外部配置文件，直接在代码中添加用户
        BaseUser user = new BaseUser(); // 创建用户
        user.setName("123");
        user.setPassword("123");
        user.setHomeDirectory("C:\\Users\\MuYang\\Desktop\\前端Vue精品课课件"); // 设置用户主目录（需真实存在）
        user.setEnabled(true); // 设置用户是否启用
        user.setMaxIdleTime(300); // 设置用户空闲超时时间（单位：秒）

        // 权限配置（示例：允许上传、下载、删除）
        ArrayList<Authority> authorities = new ArrayList<>();
        authorities.add(new WritePermission()); // 添加写权限

        user.setAuthorities(authorities); // 设置用户权限
//        user.getAuthorities().add(new TransferRatePermission()); // 设置上传和下载速率限制

        serverFactory.setUserManager(userManagerFactory.createUserManager());// 设置用户管理器
        serverFactory.getUserManager().save(user); // 保存用户

        // 4. 创建并启动服务器
        FtpServer server = serverFactory.createServer();
        server.start();
        System.out.println("FTP Server started on port 2121");
    }*/
}