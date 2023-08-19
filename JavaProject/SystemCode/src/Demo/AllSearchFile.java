package Demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/19
 * Time: 16:40
 */
public class AllSearchFile {

    public static void main(String[] args) {
        System.out.print("请输入要查找的绝对路径: ");
        Scanner scanner = new Scanner(System.in);
        File file = new File(scanner.next());
        if (!file.isDirectory()) {
            System.out.println("非法路径");
            return;
        }
        System.out.print("请输入要查找的关键字(会搜索文本文件内容): ");
        String key = scanner.next();

        List<List<String>> lists = new ArrayList<>(2);
        List<String> nameList = new LinkedList<>();
        List<String> contentList = new LinkedList<>();
        lists.add(nameList);
        lists.add(contentList);

        try {
            Search(file, key, lists);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("找到了" + lists.get(0).size() + "个包含" + key + "关键字的文件(文件名):");
        for (String temp : lists.get(0)) {
            System.out.println(temp);
        }
        System.out.println("================================================================");

        System.out.println("找到了" + lists.get(1).size() + "个包含" + key + "关键字的文件(内容):");
        for (String temp : lists.get(1)) {
            System.out.println(temp);
        }

    }


    private static void Search(File path, String key, List<List<String>> lists) throws IOException {
        File[] files = path.listFiles();
        if (files.length == 0) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                if (files[i].getName().contains(key)) {
                    //System.out.println("找到了一个包含" + key + "关键字的文件(文件名):" + files[i].getCanonicalPath());
                    lists.get(0).add(files[i].getCanonicalPath());
                } else {
                    try (InputStream inputStream = new FileInputStream(files[i]);
                         Scanner fileScanner = new Scanner(inputStream, "UTF-8")) {
                        String str = "";
                        while (fileScanner.hasNext()) {
                            str = fileScanner.next();
                            if (str.contains(key)) {
                                //System.out.println("找到了一个包含" + key + "关键字的文件(内容):" + files[i].getCanonicalPath());
                                lists.get(1).add(files[i].getCanonicalPath());
                            }
                        }
                    }
                }
            } else {
                Search(files[i], key, lists);
            }
        }
    }
}
