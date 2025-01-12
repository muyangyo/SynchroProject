package TestPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomCommentFromFile {
    public static void main(String[] args) {
        // 文件路径（确保文件路径正确）
        String filePath = "./mark/100Comments.txt";

        // 1. 读取文件内容并存储到List中
        List<String> comments = readCommentsFromFile(filePath);

        // 2. 检查是否成功读取评语
        if (comments.isEmpty()) {
            System.out.println("文件为空或读取失败，请检查文件内容！");
            return;
        }

        // 3. 随机抽取一条评语
        String randomComment = getRandomComment(comments);

        // 4. 输出结果
        System.out.println("随机抽取的评语：");
        System.out.println(randomComment);
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
        Random random = new Random();
        int randomIndex = random.nextInt(comments.size()); // 生成随机索引
        return comments.get(randomIndex);
    }
}