package Demo;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/18
 * Time: 21:44
 */
public class ForThread2 {
    public static void main1(String[] args) throws ExecutionException, InterruptedException {
        int[] ints = new int[1000_0000];
        Random random = new Random();
        for (int i = 0; i < 1000_0000; i++) {
            ints[i] = random.nextInt(100);
        }

        //开始时间
        Long startTime = System.currentTimeMillis();

        Callable<Long> callable1 = () -> {
            long count = 0;
            for (int i = 0; i < 1000_0000; i += 2) {
                count += ints[i];
            }
            return count;
        };
        FutureTask<Long> integerFuture1 = new FutureTask<>(callable1);
        Thread thread1 = new Thread(integerFuture1);


        Callable<Long> callable2 = () -> {
            long count = 0;
            for (int i = 0; i < 1000_0000; i += 2) {
                count += ints[i];
            }
            return count;
        };
        FutureTask<Long> integerFuture2 = new FutureTask<>(callable2);
        Thread thread2 = new Thread(integerFuture2);

        thread1.start();
        thread2.start();
        Long count = integerFuture1.get();
        count += integerFuture2.get();

        Long time = System.currentTimeMillis() - startTime;
        System.out.println("count = " + count);
        System.out.println("Time = " + time);
    }
}
