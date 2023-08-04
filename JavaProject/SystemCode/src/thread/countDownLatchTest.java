package thread;

import java.util.concurrent.CountDownLatch;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/4
 * Time:12:36
 */
public class countDownLatchTest {
    public static void main(String[] args) throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(10);//初始化任务数
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "完成任务");
                    latch.countDown();//任务完成,减少计数
                }
            });
            t.start();
        }
        latch.await();//allwait的简写,带阻塞功能(为 0 时 表示完成)
        System.out.println("进度:100%");

    }
}
