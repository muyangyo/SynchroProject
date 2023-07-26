package thread;

import sun.awt.windows.WToolkit;

import java.util.Scanner;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/25
 * Time:15:01
 */
public class memoryThread {
    public volatile static int isEnd = 1;

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            while (isEnd != 0) {
                isEnd++;
            }
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("请输入 0 以停止 线程1");
            Scanner scanner = new Scanner(System.in);
            isEnd = scanner.nextInt();
        });

        thread1.start();
        Thread.sleep(1000);
        thread2.start();
    }
}
