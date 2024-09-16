package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;

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
    private static ChromeDriver driver;

    static {
        //使⽤插件管理⼯具webdrivermanager
        WebDriverManager.chromedriver().setup();
        //添加浏览器配置
        Main.options = new ChromeOptions();
        //允许任何来源的远程连接
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(Main.options);
//        driver.get(BAI_DU_URL);
    }

    @BeforeAll
    static void start() {
        System.out.println("测试前");
    }

    @Test
    void Test() {
        System.out.println("测试开始");
    }

    public static void main11(String[] args) {
        driver.get("http://localhost:63342/TestDemo/Page/test05.html?_ijt=mfmmrktu3mem3s53b4fp8uirrn&_ij_reload=RELOAD_ON_SAVE");
        WebElement element = driver.findElement(By.cssSelector("body > input[type=file]"));
        element.sendKeys("");
    }

    public static void main10(String[] args) throws InterruptedException {
        driver.get("http://localhost:63342/TestDemo/Page/test04.html?_ijt=cfssvrdogjnk7pteop6i77dsuf&_ij_reload=RELOAD_ON_SAVE");
        WebElement element = driver.findElement(By.cssSelector("body > button"));
        element.click();
        Alert alert = driver.switchTo().alert();
        alert.sendKeys("你好");
        alert.accept();
        sleep(3000);

        element.click();
        alert.sendKeys("123");
        sleep(3000);
        alert.dismiss();
    }

    public static void main9(String[] args) throws InterruptedException {
        driver.get("http://localhost:63342/TestDemo/Page/test03.html?_ijt=3kknu2bau5o7642ddpiuq35f5t&_ij_reload=RELOAD_ON_SAVE");
        WebElement element = driver.findElement(By.cssSelector("#ShippingMethod"));
        Select select = new Select(element);
        //通过 下标 进行选择,下标从0开始
        select.selectByIndex(2);
        sleep(3000);
        select.selectByIndex(0);
        sleep(3000);
        //通过 value 属性进行选择
        select.selectByValue("9.03");
        sleep(3000);
        driver.quit();
    }

    public static void main8(String[] args) throws InterruptedException {
        driver.get("http://localhost:63342/TestDemo/Page/test01.html?_ijt=ul37a6102s1566067jeih6ials&_ij_reload=RELOAD_ON_SAVE");
        List<WebElement> elements = driver.findElements(By.xpath("//input[@type='checkbox']"));
        for (WebElement temp : elements) {
            temp.click();
        }
        sleep(3000);
        for (WebElement temp : elements) {
            temp.click();
        }
    }

    public static void main7(String[] args) throws IOException {
        File screenshot = (((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE));
        FileUtils.copyFile(screenshot, new File("C:/Users/MuYang/Desktop/1.png"));
    }


    public static void main6(String[] args) throws InterruptedException {
        driver.get("http://localhost:63342/TestDemo/Page/test02.html?_ijt=h9aqc9nlhui3c48fs7oj4t2b6a");
        sleep(2000);
        try {
            driver.findElement(By.cssSelector("body > div > div > a")).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.switchTo().frame("f1");
        sleep(2000);
        driver.findElement(By.cssSelector("body > div > div > a")).click();
    }

    public static void main5(String[] args) throws InterruptedException {
        sleep(4000);
        //点击这个元素后会新建一个页面
        WebElement element = driver.findElement(By.cssSelector("#s-top-left > a:nth-child(1)"));
        element.click();
        sleep(4000);
        String curWindow = driver.getWindowHandle();//获取当前窗口
        System.out.println(curWindow);//打印当前窗口

        WebElement oldE = driver.findElement(By.cssSelector("#s-top-left > a:nth-child(1)"));
        if (oldE.isEnabled()) System.out.println("依旧可以找到旧元素");


        System.out.println("================= 切换窗口中... ================");
        Set<String> windowHandles = driver.getWindowHandles();
        for (String temp : windowHandles) {
            if (!temp.equals(curWindow)) driver.switchTo().window(temp); //切换窗口
        }

        WebElement newE = driver.findElement(By.cssSelector("#header-link-wrapper > li:nth-child(2) > span"));
        if (newE.isEnabled()) System.out.println("找到了新元素");

        driver.quit();
    }

    public static void main4(String[] args) throws InterruptedException {
        ChromeDriver driver = new ChromeDriver(Main.options);
        driver.get(BAI_DU_URL);

        WebElement element = driver.findElement(By.cssSelector("#s-top-left > a:nth-child(1)"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).contextClick().perform();
        sleep(4000);
        driver.quit();
    }

    public static void main3(String[] args) throws InterruptedException {
        ChromeDriver driver = new ChromeDriver(Main.options);
        driver.get(BAI_DU_URL);

        WebElement element = driver.findElement(By.cssSelector("#kw"));
        element.sendKeys("测试");
        sleep(2000);
        element.sendKeys(Keys.ENTER);
        sleep(2000);
        element.sendKeys(Keys.CONTROL, "A");
        sleep(1000);
        element.sendKeys(Keys.CONTROL, "X");
        sleep(1000);
        driver.quit();
    }

    public static void main2(String[] args) throws InterruptedException {
        ChromeDriver chromeDriver = new ChromeDriver(Main.options);
        chromeDriver.get(BAI_DU_URL);

        chromeDriver.manage().window().maximize();
        sleep(2000);
        chromeDriver.manage().window().minimize();
        sleep(2000);
        chromeDriver.manage().window().setSize(new Dimension(200, 200));
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