package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/23
 * Time:22:30
 */
public class threadGet extends Thread {
    public threadGet(String str) {
        super(str);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Thread thread = new threadGet("myThread");
        thread.start();
    }
}
