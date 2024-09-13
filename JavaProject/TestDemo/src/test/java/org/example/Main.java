package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: ${DATE}
 * Time: ${TIME}
 */
public class Main {
    public static final String BAI_DU_URL = "https://www.baidu.com";

    private static ChromeOptions options;

    static {
        //使⽤插件管理⼯具webdrivermanager
        WebDriverManager.chromedriver().setup();
        //添加浏览器配置
        Main.options = new ChromeOptions();
        //允许任何来源的远程连接
        options.addArguments("--remote-allow-origins=*");
    }

    public static void main(String[] args) throws InterruptedException {
        ChromeDriver chromeDriver = new ChromeDriver(Main.options);
        chromeDriver.get(BAI_DU_URL);

        chromeDriver.manage().window().maximize();
        sleep(2000);
        chromeDriver.manage().window().minimize();
        sleep(2000);
        chromeDriver.manage().window().setSize(new Dimension(200,200));
        sleep(2000);
    }

    public static void main1(String[] args) {
        //使⽤插件管理⼯具webdrivermanager
        WebDriverManager.chromedriver().setup();
        //添加浏览器配置
        ChromeOptions options = new ChromeOptions();
        //允许任何来源的远程连接
        options.addArguments("--remote-allow-origins=*");

        ChromeDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.get("https://www.baidu.com");
        WebElement element = chromeDriver.findElement(By.cssSelector("#kw"));

        element.sendKeys("软件测试");
        WebElement btn = chromeDriver.findElement(By.xpath("//*[@id=\"su\"]"));
        btn.click();
        List<WebElement> elements = chromeDriver.findElements(By.cssSelector("a em"));
        if (elements == null) System.out.println("查找失败!");
        System.out.println(elements.size());
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // 隐式等待5秒
        elements = chromeDriver.findElements(By.cssSelector("a em"));
        if (elements == null) System.out.println("查找失败!");
        System.out.println(elements.size());
    }

    public static void test1(String[] args) throws InterruptedException {
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
        //获取一个元素
        WebElement element = chromeDriver.findElement(By.cssSelector("#kw"));//通过 CSS的ID 选择器获取对象
        //模拟键盘输入
        element.sendKeys("测试1");
        sleep(3000);
        //清除输入的内容
        element.clear();
        sleep(3000);
        //模拟键盘输入
        element.sendKeys("软件测试");
        //通过xpath获取到搜索按钮
        WebElement btn = chromeDriver.findElement(By.xpath("//*[@id=\"su\"]"));
        //点击
        btn.click();
        sleep(3000);
        //获取当前页面的标题
        System.out.println(chromeDriver.getTitle());
        //获取当前页面的URL
        System.out.println(chromeDriver.getCurrentUrl());
        //结束浏览器进程并清理缓存
        chromeDriver.quit();
        //关闭当前窗口(不推荐)
        //chromeDriver.close();
    }

    public static void test2() throws InterruptedException {
        //使⽤插件管理⼯具webdrivermanager
        WebDriverManager.chromedriver().setup();
        //添加浏览器配置
        ChromeOptions options = new ChromeOptions();
        //允许任何来源的远程连接
        options.addArguments("--remote-allow-origins=*");
        ChromeDriver chromeDriver = new ChromeDriver(options);
        // 打开 百度 首页
        chromeDriver.get("https://www.baidu.com");
        // 获取一个元素
        WebElement element = chromeDriver.findElement(By.cssSelector("#kw")); // 通过 CSS的ID 选择器获取对象
        // 模拟键盘输入
        element.sendKeys("测试1");
        sleep(5000); // 假设这是用来等待页面或元素响应的自定义方法
        // 使用getAttribute("value")来获取输入框中的值
        String inputText = element.getAttribute("value");
        System.out.println(inputText); // 输出: 测试1
        if (!"测试1".equals(inputText)) {
            // 处理不一致的情况
            System.out.println("获取不到文本");
        }
    }

}