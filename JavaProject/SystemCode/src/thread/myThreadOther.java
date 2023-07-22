package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/21
 * Time:17:56
 */

public class myThreadOther implements Runnable {
    @Override
    public void run() {
        System.out.println("Test");
    }

    public static void main(String[] args) {
        myThreadOther mythreadother = new myThreadOther();
        Thread thread = new Thread(mythreadother);
        thread.start();//Runnable中并没有这些方法,只能利用 Thread 实现
    }

}
