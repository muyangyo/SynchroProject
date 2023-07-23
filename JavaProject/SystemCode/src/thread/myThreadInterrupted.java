package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/23
 * Time:22:46
 */
public class myThreadInterrupted extends Thread {

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("myThread");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("线程将继续执行!");
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new myThreadInterrupted();
        thread.start();//线程开始执行

        //主线程休息
        Thread.sleep(3000);

        //终止 thread 线程
        thread.interrupt();
    }

}
