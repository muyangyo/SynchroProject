package File;

import java.io.*;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/2
 * Time:16:56
 */
public class fileOperationTest {
    public static void main(String[] args) throws IOException {
        File file = new File("./Test.txt");
        System.out.println(file.getParent());
        System.out.println(file.getName());
        System.out.println(file.getPath());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getCanonicalPath());
        System.out.println(file.exists());

        System.out.println(file.isDirectory());
        System.out.println(file.isFile());

        File file1 = new File("./E/X/P");
        file1.mkdirs();
        File file2 = new File(file1.getPath() + "/test.txt");
        file2.createNewFile();
        File file3 = new File("./E/X/text2.txt");
        file2.renameTo(file3);
    }
}
