package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/21
 * Time:16:42
 */

//Thread 源自 java.lang包里(和 String 一样,不需要显示导入)
public class myThread extends Thread {
    @Override
    public void run() {
        System.out.println("Test");
        try {
            Thread.sleep(1000);//线程休眠,单位 ms,只能用 try-catch 包围
        } catch (InterruptedException e) {
            throw new RuntimeException("睡眠EOR");
        }
    }

    public static void main(String[] args) {
        myThread mythread = new myThread();
        mythread.start();//创建新线程并运行run方法,主线程则会 继续 执行后面的指令
        //mythread.run();//没有创建新线程,只是跑了一个方法而已
    }
}
