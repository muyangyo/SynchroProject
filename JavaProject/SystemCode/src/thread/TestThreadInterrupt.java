package thread;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/5
 * Time:22:01
 */
public class TestThreadInterrupt {
    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().isInterrupted());
                Thread.interrupted();//清楚标识
            }
    });
        thread.start();
        Thread.sleep(1000);
        thread.interrupt();
}
}
