package thread;

import com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/2
 * Time:9:24
 */
public class callableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int ret = 0;
                for (int i = 0; i < 100; i++) {
                    ret++;
                }
                return ret;
            }
        };
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread thread = new Thread(futureTask);

        thread.start();
        int ret = futureTask.get();//带 wait 效果,会阻塞 主线程 等 其 算出答案
        System.out.println(ret);


    }
}
