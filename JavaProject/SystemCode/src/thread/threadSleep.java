package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/23
 * Time:22:26
 */
public class threadSleep {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("Test");
        });
        thread.start();
        Thread.sleep(1000);
    }

}
