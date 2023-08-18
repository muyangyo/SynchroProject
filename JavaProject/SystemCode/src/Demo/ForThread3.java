package Demo;

import java.util.concurrent.CountDownLatch;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/18
 * Time: 22:04
 */
public class ForThread3 {


    public static void main1(String[] args) {
        ForThread3 forthread3 = new ForThread3();
        CountDownLatch countDownLatch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(() -> {
                synchronized (forthread3) {
                    System.out.println(forthread3.count);
                    forthread3.count++;
                    countDownLatch.countDown();
                }
            });
            thread.start();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("ok");
    }

    volatile int count = 0;

    public static void main2(String[] args) {
        ForThread3 forThread3 = new ForThread3();
        Thread thread1 = new Thread(() ->
        {
            synchronized (forThread3) {
                for (int i = 0; i < 10000; i++) {
                    forThread3.count++;
                }
            }
        });

        Thread thread2 = new Thread(() ->
        {
            synchronized (forThread3) {
                for (int i = 0; i < 10000; i++) {
                    forThread3.count++;
                }
            }
        });

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
            System.out.println(forThread3.count);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100_0000; i++) {
            Thread thread1 = new Thread(() -> {
                System.out.print("a");
            });

            Thread thread2 = new Thread(() -> {
                try {
                    System.out.print("b");
                    thread1.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            Thread thread3 = new Thread(() -> {
                try {
                    thread2.join();
                    System.out.print("c");
                    System.out.println();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread1.start();
            thread2.start();
            thread3.start();
            try {
                thread3.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

