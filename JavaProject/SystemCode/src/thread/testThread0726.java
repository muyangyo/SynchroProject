package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/26
 * Time:19:01
 */
public class testThread0726 {

    static Object locker = new Object();


    public static void main(String[] args) {

        Thread thread1 = new Thread(() -> {
            synchronized (locker) {
                System.out.println("=====线程1=====");
                System.out.println("thread1正在执行任务!");
                //执行任务代码
                System.out.println("thread1进入等待状态");
                try {
                    locker.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("=====线程1=====");
                System.out.println("thread1已被唤醒!");
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (locker) {
                System.out.println("=====线程2=====");
                System.out.println("thread2正在执行任务!");
                //执行任务代码
                System.out.println("thread2已执行完任务,准备唤醒等待线程");
                locker.notify();
            }
        });

        thread1.start();
        thread2.start();

    }
}
