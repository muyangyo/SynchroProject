package Demo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/19
 * Time: 11:52
 */
public class Pool1 {
    BlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>();//任务队列

    public Pool1(int n) {
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        runnables.take().run();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }
    }

    public void submit(Runnable r) {
        try {
            runnables.put(r);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        Pool1 pool1 = new Pool1(2);
        pool1.submit(() -> {
            int count = 0;
            for (int i = 0; i < 10000; i++) {
                count++;
            }
            System.out.println(count);
        });

        pool1.submit(() -> {
            int count = 0;
            for (int i = 0; i < 10000; i++) {
                count++;
            }
            System.out.println(count);
        });
    }


}
