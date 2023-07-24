package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/24
 * Time:20:40
 */

class Example {

}

public class synchronizedDemo {
    public int count = 0;


    public static void main(String[] args) throws InterruptedException {

        synchronizedDemo synchronizeddemo = new synchronizedDemo();

        Example example = new Example();//随便 new 一个对象,用于加锁(用synchronizeddemo也可)

        Thread thread1 = new Thread(() -> {
            synchronized (example) {
                for (int i = 0; i < 100000; i++) {
                    synchronizeddemo.count++;
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (example) {
                for (int i = 0; i < 100000; i++) {
                    synchronizeddemo.count++;
                }
            }
        });
        //同时开始执行
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(synchronizeddemo.count);//预期 2000
    }
}
