package Demo;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/19
 * Time: 15:34
 */
public class SearchFile {
    public static void main(String[] args) throws IOException {
        System.out.print("请输入需要查询的目录路径:");
        Scanner scanner = new Scanner(System.in);
        File path = new File(scanner.next());
        if (!path.isDirectory()) {
            System.out.println("非法路径!");
            return;
        }
        System.out.print("请输入要删除的文件关键字:");
        String name = scanner.next();
        Search(path, name);
    }

    private static void Search(File path, String del) throws IOException {
        File[] files = path.listFiles();
        if (files.length == 0) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                if (files[i].getName().contains(del)) {
                    System.out.println("找到了一个包含" + del + "关键字的文件:" + files[i].getCanonicalPath());
                    System.out.print("是否要删除? (Y/N) ");
                    Scanner sc = new Scanner(System.in);
                    String c = sc.next();
                    if (c.equals("Y") || c.equals("y")) {
                        files[i].delete();
                    }
                }
            } else {
                Search(files[i], del);
            }
        }
    }
}
