package Demo;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/19
 * Time: 14:46
 */
public class Test1 {
    private volatile static int count = 0;

    public static void main1(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                try {
                    semaphore.acquire();
                    Test1.count++;
                    semaphore.release();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                try {
                    semaphore.acquire();
                    Test1.count++;
                    semaphore.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(Test1.count);
    }

    public static void main2(String[] args) throws ExecutionException, InterruptedException {
        Callable<Integer> integerCallable = () -> {
            int ret = 0;
            for (int i = 1; i < 1000; i++) {
                ret += i;
            }
            return ret;
        };
        FutureTask<Integer> integerFutureTask = new FutureTask<>(integerCallable);
        Thread thread = new Thread(integerFutureTask);
        thread.start();
        System.out.println(integerFutureTask.get());
    }

    public static void main3(String[] args) throws InterruptedException {
        AtomicInteger integer = new AtomicInteger(0);
        Thread thread1 = new Thread(() ->
        {
            for (int i = 0; i < 100000; i++) {
                integer.incrementAndGet();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                integer.incrementAndGet();
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(integer);
    }

    public static void main(String[] args) {
        BlockingQueue<Runnable> runnables = new ArrayBlockingQueue<>(100);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10,
                100, TimeUnit.MILLISECONDS, runnables, new ThreadPoolExecutor.DiscardPolicy());
    }
}
