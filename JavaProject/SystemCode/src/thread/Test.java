package thread;

// 后台线程 和 前台线程
public class Test extends Thread {

    @Override
    public void run() {
        while (true) {
            System.out.println("hello thread");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Test();
        t.setDaemon(true);// 设置成后台线程了
        t.start();

        Thread.sleep(3000);//睡眠一会后, 主线程 结束, 后台进程 跟随自动结束
    }
}