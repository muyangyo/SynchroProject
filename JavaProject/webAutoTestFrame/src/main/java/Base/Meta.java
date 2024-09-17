package Base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/17
 * Time: 14:46
 */
public class Meta {


    /**
     * 为什么这里使用的是静态呢? 因为避免new对象才进行初始化,直接调用就进行初始化
     */
    static {
        //使⽤插件管理⼯具webdrivermanager
        WebDriverManager.chromedriver().setup();
        //添加浏览器配置
        ChromeOptions options = new ChromeOptions();
        //允许任何来源的远程连接
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @RequiresInitialization
    public static void init(WaitStrategy strategy) {
        if (strategy.equals(WaitStrategy.INTELLIGENT_WAITING)) {
            //智能等待
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));//隐式等待2秒
        }
        sleep = new Sleep();
    }

    public static ChromeDriver driver;
    public static Sleep sleep;


    public static WebElement byCssGetElement(String cssSelector) {
        return driver.findElement(By.cssSelector(cssSelector));
    }

    public static WebElement byXpathGetElement(String xpath) {
        return driver.findElement(By.xpath(xpath));
    }
}
