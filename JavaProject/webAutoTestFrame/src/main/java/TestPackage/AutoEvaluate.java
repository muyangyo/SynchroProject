package TestPackage;

import Base.Meta;
import Base.WaitStrategy;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/1/12
 * Time: 12:06
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutoEvaluate extends Meta {
    private static List<String> comments;

    @BeforeAll
    public static void beforeAll() throws IOException {
        init(WaitStrategy.INTELLIGENT_WAITING);
        // 文件路径（确保文件路径正确）
        String filePath = "./mark/100Comments.txt";

        // 1. 读取文件内容并存储到List中
        comments = readCommentsFromFile(filePath);

        // 2. 检查是否成功读取评语
        if (comments.isEmpty()) {
            throw new IOException("文件为空或读取失败，请检查文件内容！");
        }
    }

    @CsvFileSource(resources = "Demo1.csv")
    @Order(0)
    @ParameterizedTest
    public void test1(String username, String password) throws InterruptedException {
        driver.get("https://gzzs.jxedu.gov.cn/login");

        WebElement usernameInput = byXpathGetElement("//*[@id=\"app\"]/div/div[1]/div[1]/form/div[2]/div[1]/div/div[1]/input");
        usernameInput.sendKeys(username);
        sleep.sleepNormalTime();

        WebElement passwordInput = byXpathGetElement("//*[@id=\"app\"]/div/div[1]/div[1]/form/div[2]/div[2]/div/div/input");
        passwordInput.sendKeys(password);
        sleep.sleepNormalTime();

        WebElement captchaInput = byXpathGetElement("//*[@id=\"app\"]/div/div[1]/div[1]/form/div[2]/div[3]/div/div[1]/input");
        captchaInput.sendKeys("");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 超时时间为10秒
        wait.until(driver -> { // WebDriverWait 会定期检查传入的条件（通常是一个 ExpectedCondition 或 Lambda 表达式），直到条件为 true 或超时
            String value = captchaInput.getAttribute("value");
            return value != null && value.length() == 4; // 检查长度是否为 4
        });

        WebElement loginButton = byXpathGetElement("//*[@id=\"app\"]/div/div[1]/div[1]/form/div[2]/div[6]/div/button");
        loginButton.click();
        sleep.sleepNormalTime();

        WebElement evaluatePageBtn = byXpathGetElement("//*[@id=\"app\"]/div/div[3]/section/div/div/div[1]/div[1]/div/div[3]/div/img");
        evaluatePageBtn.click();
        sleep.sleepNormalTime();

        int i = 0;
        while (true) {

            List<WebElement> rows = driver.findElements(By.cssSelector("tr.el-table__row"));
            if (rows.isEmpty()) {
                System.out.println("表格数据已全部处理完毕。");
                break;
            }
            WebElement curRow = rows.get(i);

            // 检查第八列内容
            WebElement statusElement = curRow.findElement(By.cssSelector("td.el-table_1_column_8 .cell span"));
            String status = statusElement.getText().trim();

            // 如果第八列是 "待评价"，则跳过该行
            if ("待评价".equals(status)) {
                System.out.println("跳过当前行，状态为: " + status);
                System.out.println();
                i++;
                continue;
            }


            // 提取学年
            WebElement schoolYear = curRow.findElement(By.cssSelector("td.el-table_1_column_3 .cell"));
            String schoolYearText = schoolYear.getText().trim();

            // 提取学期
            WebElement term = curRow.findElement(By.cssSelector("td.el-table_1_column_4 .cell"));
            String termText = term.getText().trim();

            // 提取姓名
            WebElement studentName = curRow.findElement(By.cssSelector("td.el-table_1_column_6 .cell"));
            String studentNameText = studentName.getText().trim();

            // 生成随机评价
            String randomComment = getRandomComment(comments);
            System.out.println(studentNameText + " 的 " + schoolYearText + " " + termText + " 评价为: [" + randomComment + "]");
            System.out.println();

            // 点击评价按钮
            WebElement assessmentDiv = curRow.findElement(By.cssSelector("td.el-table_1_column_10 .cell button:nth-child(2) span"));
            assessmentDiv.click();
            sleep.sleepNormalTime();

            // 填写评价
            WebElement textArea = driver.findElement(By.cssSelector("body > div.el-dialog__wrapper.dialog > div > div.el-dialog__body > form > div > div > div.el-textarea.el-input--medium > textarea"));
            textArea.sendKeys(randomComment);
            sleep.sleepShortTime();


            // 提交评价
            WebElement submitBtn = driver.findElement(By.cssSelector("body > div.el-dialog__wrapper.dialog > div > div.el-dialog__footer > div > button.el-button.el-button--primary.el-button--medium > span"));
            submitBtn.click();
            sleep.sleepNormalTime();
        }
    }

    @AfterAll
    public static void afterAll() {
//        driver.quit();
    }

    /**
     * 从文件中读取评语并存储到List中
     *
     * @param filePath 文件路径
     * @return 包含所有评语的List
     */
    private static List<String> readCommentsFromFile(String filePath) {
        List<String> comments = new ArrayList<>(100);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) { // 忽略空行
                    comments.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("读取文件时出错: " + e.getMessage());
        }
        return comments;
    }

    /**
     * 从List中随机抽取一条评语
     *
     * @param comments 包含所有评语的List
     * @return 随机抽取的一条评语
     */
    private static String getRandomComment(List<String> comments) {
        Random random = new Random(System.currentTimeMillis());
        int randomIndex = random.nextInt(comments.size());
        return comments.get(randomIndex);
    }
}
