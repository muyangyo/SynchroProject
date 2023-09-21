package thread;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/21
 * Time: 12:28
 */
class MySocket {
    int Cache = 0;
}

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        MySocket mySocket = new MySocket();
        //模拟持续对 socket文件 进行写操作
        Thread thread1 = new Thread(() -> {
            //加锁
            synchronized (Demo.class) {
                for (int i = 0; i < 100000; i++) {
                    mySocket.Cache++;//这里既是为了模拟也是为了解决变量捕获机制带来的问题
                }
            }
        });//这里使用了lambda表达式进行简化
        Thread thread2 = new Thread(() -> {
            //加锁
            synchronized (Demo.class) {
                for (int i = 0; i < 100000; i++) {
                    mySocket.Cache++;//这里既是为了模拟也是为了解决变量捕获机制带来的问题
                }
            }
        });
        //预期 20w (有人可能会问:我自己写的又不会这么频繁,也不会这么大量,又没关系.那我只能说:你学的技术就没想着为了就业)
        thread1.start();
        thread2.start();

        thread1.join();//阻塞主线程,等待thread1结束
        thread2.join();//阻塞主线程,等待thread1结束
        System.out.println(mySocket.Cache);
    }
}
