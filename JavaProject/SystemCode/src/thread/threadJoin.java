package thread;

import java.sql.SQLOutput;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/23
 * Time:23:22
 */
public class threadJoin extends Thread {

    public threadJoin(String str) {
        super(str);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程启动");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new threadJoin("线程1");
        Thread thread2 = new threadJoin("线程2");
        thread1.start();
        thread1.join();//此时主线程 阻塞
        System.out.println("线程1 已经处理完,启动 线程2 !");
        thread2.start();
        thread2.join();//此时主线程 阻塞
        System.out.println("线程2 已经处理完,主线程 也将结束");
    }
}
