package Mode;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/1
 * Time:11:00
 */
public class myPool {
    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    //通过这个方法,来把任务添加到线程池中.
    public void submit(Runnable runnable) throws InterruptedException {
        queue.put(runnable); //自带阻塞功能,不需要考虑 满 的情况
    }

    // n 表示线程池里有几个线程,创建了一个固定数量的线程池.
    public myPool(int n) {
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        queue.take().run();//自带阻塞功能,不需要考虑 空 的情况
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        }

    }


    public static void main1() {

        //创建固定线程数的线程池
        ExecutorService service1 = Executors.newFixedThreadPool(10);
        service1.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        });
        //创建线程数目动态增长的线程池
        ExecutorService service2 = Executors.newCachedThreadPool();
        //创建只有一个线程的线程池
        ExecutorService service3 = Executors.newSingleThreadExecutor();
        //设定 延迟时间后执行命令，或者定期执行命令. 是进阶版的 Timer.
        ExecutorService service4 = Executors.newScheduledThreadPool(5);

    }
}
