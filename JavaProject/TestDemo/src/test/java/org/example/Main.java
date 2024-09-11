package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: ${DATE}
 * Time: ${TIME}
 */
public class Main {
    public static void main(String[] args) {
        //使⽤插件管理⼯具webdrivermanager
        WebDriverManager.chromedriver().setup();
        //添加浏览器配置
        ChromeOptions options = new ChromeOptions();
        //允许任何来源的远程连接
        options.addArguments("--remote-allow-origins=*");
        //链接驱动器
        ChromeDriver chromeDriver = new ChromeDriver(options);
        //打开 百度 首页
        chromeDriver.get("https://www.baidu.com");
    }
}