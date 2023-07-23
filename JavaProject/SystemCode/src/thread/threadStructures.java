package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/23
 * Time:22:13
 */
public class threadStructures {

    public static void main(String[] args) {

        /* Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Run-Test");
            }
        }, "Test1"); */

        Thread thread = new Thread(() -> {
            System.out.println("Run-Test");
        }, "Test1");
        thread.start();

    }
}
