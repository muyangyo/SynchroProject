package Demo;

import java.io.*;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/19
 * Time: 15:59
 */
public class CopyFile {
    public CopyFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要拷贝的文件的绝对路径:");
        File sourceFile = new File(scanner.next());
        if (!sourceFile.isFile() || !sourceFile.exists()) {
            System.out.println("非法路径");
            return;
        }
        System.out.print("请输入目标路径: ( N 可取消复制) ");
        String str = scanner.next();
        if (str.equals("n") || str.equals("N")) {
            System.out.println("已经取消复制!");
            return;
        }
        File destFile = new File(str);
        if (destFile.isDirectory()) {
            System.out.println("非法路径!");
            return;
        }
        if (destFile.exists()) {
            System.out.println("文件已经存在!是否继续? (Y/N) ");
            if (scanner.next().toUpperCase().equals("N")) return;
        }
        try (InputStream inputStream = new FileInputStream(sourceFile);
             OutputStream outputStream = new FileOutputStream(destFile);
        ) {
            while (true) {
                byte[] bytes = new byte[1024];
                int n = inputStream.read(bytes);
                if (n == -1) {
                    System.out.println("复制完成!");
                    break;
                }

                outputStream.write(bytes);
                outputStream.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        CopyFile copyFile = new CopyFile();

    }
}
