package thread;

import java.util.concurrent.Semaphore;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/4
 * Time:12:26
 */
public class semaphoreTest {
    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(3);//只有 3 个 该类型的资源
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("申请资源中");
                        semaphore.acquire();//申请资源,信号量为 0 时,阻塞
                        System.out.println("获取到资源了");
                        //任务体
                        System.out.println("正在释放资源");
                        semaphore.release();//释放资源
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();
        }

    }
}
