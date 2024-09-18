package TestPackage;

import Base.Meta;
import Base.WaitStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/17
 * Time: 15:20
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HubSystemTest extends Meta {
    static int anInt = 0;

    @BeforeAll
    static void Init() throws IOException {
        init(WaitStrategy.INTELLIGENT_WAITING);
        File file = new File("./mark/mark.txt");
        if (!file.exists()) {
            FileUtils.touch(file);
        }

        try (InputStream inputStream = new FileInputStream(file);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             Scanner scanner = new Scanner(inputStreamReader)) {
            if (scanner.hasNextInt()) {
                anInt = scanner.nextInt();
            }
        }

        try (OutputStream outputStream = new FileOutputStream(file);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
             PrintWriter printWriter = new PrintWriter(outputStreamWriter)) {
            printWriter.println(anInt + 1);
            printWriter.flush();
        }

    }

    @ParameterizedTest
    @CsvSource({"'user','user'"})
    @Order(0)
    void registerTest(String newUsername, String newPassword) throws InterruptedException {
        newUsername = newUsername + anInt;
        newPassword = newPassword + anInt;

        driver.get("http://127.0.0.1:8080/");
        WebElement registerBtn = byCssGetElement("body > div.login-container > div > div:nth-child(4) > a");
        registerBtn.click();
        sleep.sleepNormalTime();
        Assertions.assertEquals("http://127.0.0.1:8080/register.html", driver.getCurrentUrl());
        WebElement username = byCssGetElement("#inputUsername");
        username.sendKeys(newUsername);
        WebElement password = byCssGetElement("#inputPassword");
        password.sendKeys(newPassword);
        WebElement rePassword = byCssGetElement("#inputREPassword");
        rePassword.sendKeys(newPassword + "1");
        WebElement submit = byCssGetElement("#registerButton");
        submit.click();
        sleep.sleepNormalTime();
        //验证如果密码不同是否会报错
        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("两次输入的密码不一致！", alert.getText());
        alert.accept();
        sleep.sleepNormalTime();

        //重新输入密码
        rePassword.clear();
        rePassword.sendKeys(newPassword);
        submit.click();
        sleep.sleepNormalTime();
        Alert alert1 = driver.switchTo().alert();
        Assertions.assertEquals("注册成功!", alert1.getText());
        alert1.accept();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "login.csv")
    @Order(1)
    void loginTest(String username, String password) throws InterruptedException {
        driver.get("http://127.0.0.1:8080/");
        WebElement usernameText = byCssGetElement("#username");
        usernameText.sendKeys(username);
        WebElement passwordText = byCssGetElement("#password");
        passwordText.sendKeys(password);
        WebElement submit = byCssGetElement("#submit");
        submit.click();
        sleep.sleepNormalTime();
        driver.switchTo().alert().accept();

        Assertions.assertEquals("http://127.0.0.1:8080/blog_list.html", driver.getCurrentUrl());
        Assertions.assertEquals(username, byCssGetElement("body > div.container > div.container-left > div > h3").getText());
    }

    @Test
    @Order(2)
    void publishBlogTest() throws InterruptedException {
        driver.get("http://127.0.0.1:8080/blog_list.html");
        WebElement a = byCssGetElement("body > div.nav > a:nth-child(5)");
        a.click();
        sleep.sleepNormalTime();
        WebElement title = byCssGetElement("#title-input");
        title.sendKeys("自动化测试生成文章");

        WebElement submit = byCssGetElement("#submit");
        submit.click();
        sleep.sleepShortTime();

        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("成功添加!", alert.getText());
        alert.accept();
    }

    @Test
    @Order(3)
    void commentTest() throws InterruptedException {
        driver.get("http://127.0.0.1:8080/blog_list.html");
        WebElement blog = byXpathGetElement("//div[text()='自动化测试生成文章']");
        blog.click();
        sleep.sleepNormalTime();
        WebElement text = byCssGetElement("body > div.container > div.container-left > div.commentArea > div > input");
        text.sendKeys("测试评论是否正常");
        WebElement btn = byCssGetElement("body > div.container > div.container-left > div.commentArea > div > button");
        btn.click();
        sleep.sleepNormalTime();
        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("成功添加评论!", alert.getText());
        alert.accept();
        sleep.sleepNormalTime();
        driver.navigate().refresh();
        sleep.sleepNormalTime();
        WebElement webElement = byXpathGetElement("//p[text()='测试评论是否正常']");
    }

    @Test
    @Order(4)
    void editBlogTest() throws InterruptedException {
        driver.get("http://127.0.0.1:8080/blog_list.html");
        WebElement blog = byXpathGetElement("//div[text()='自动化测试生成文章']");
        blog.click();
        sleep.sleepNormalTime();
        WebElement editBtn = byCssGetElement("body > div.container > div.container-left > div.card > button.edit-button");
        editBtn.click();
        sleep.sleepLongTime();
        WebElement title = byCssGetElement("#title-input");
        String text = title.getAttribute("value") + "修改后的";
        title.clear();
        title.sendKeys(text);
        WebElement submit = byCssGetElement("#submit");
        submit.click();
        sleep.sleepNormalTime();
        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("成功修改!", alert.getText());
        alert.accept();
        sleep.sleepNormalTime();
        WebElement newTitle = byCssGetElement("body > div.container > div.container-right > h3");
        Assertions.assertEquals(text, newTitle.getText());
    }

    @Test
    @Order(5)
    void deleteBlogTest() throws InterruptedException {
        driver.get("http://127.0.0.1:8080/blog_list.html");
        WebElement blog = byXpathGetElement("//div[text()='自动化测试生成文章修改后的']");
        blog.click();
        sleep.sleepNormalTime();
        WebElement deleteBtn = byCssGetElement("body > div.container > div.container-left > div.card > button.delete-button");
        deleteBtn.click();
        sleep.sleepShortTime();
        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("成功删除!", alert.getText());
        alert.accept();
    }

    @Test
    @Order(6)
    void logoutTest() throws InterruptedException {
        driver.get("http://127.0.0.1:8080/blog_list.html");
        WebElement logoutBtn = byCssGetElement("body > div.nav > a:nth-child(6)");
        logoutBtn.click();
        sleep.sleepNormalTime();
        Assertions.assertEquals("http://127.0.0.1:8080/login.html", driver.getCurrentUrl());

        //未登入状态下进入首页
        driver.get("http://127.0.0.1:8080/blog_list.html");
        Assertions.assertEquals("请先登录!", driver.switchTo().alert().getText());
        driver.switchTo().alert().accept();
        Assertions.assertEquals("http://127.0.0.1:8080/login.html", driver.getCurrentUrl());
    }

    @ParameterizedTest
    @CsvSource({"'Demo','123'"})
    @Order(7)
    void errorLoginTest(String username, String password) throws InterruptedException {
        driver.get("http://127.0.0.1:8080/");
        WebElement usernameText = byCssGetElement("#username");
        usernameText.sendKeys(username);
        WebElement passwordText = byCssGetElement("#password");
        passwordText.sendKeys(password);
        WebElement submit = byCssGetElement("#submit");
        submit.click();
        sleep.sleepNormalTime();

        Alert alert = driver.switchTo().alert();
        Assertions.assertEquals("登录失败! 账号或者密码错误!", alert.getText());
        sleep.sleepNormalTime();
        alert.accept();
    }

    @AfterAll
    static void close() {
        driver.quit();
    }

}
