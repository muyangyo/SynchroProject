package Demo;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/18
 * Time: 21:30
 */
class myThread1 extends Thread {
    @Override
    public void run() {
        System.out.println("继承于Thread");
    }
}

class myThread2 implements Runnable {
    @Override
    public void run() {
        System.out.println("实现于Runnable");
    }
}

public class ForThread {
    public static void main(String[] args) {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                System.out.println("匿名继承于Thread");
            }
        };

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名实现于Runnable");
            }
        });

        Thread thread3 = new Thread(() -> {
            System.out.println("lambda表达式");
        });

        Thread thread4 = new myThread1();

        Thread thread5 = new Thread(new myThread2());

        thread5.start();
        thread4.start();
        thread1.start();
        thread2.start();
        thread3.start();

    }
}
