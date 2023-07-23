package thread;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/23
 * Time:22:46
 */
public class myThreadInterruptedPrinciple {

    public static boolean isQuit = false;// 写作 成员变量 就不是触发 变量捕获 的逻辑,而是"内部类访问外部类的成员".

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {

            while (!isQuit) {
                System.out.println("hello thread");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();

        // 主线程这里执行一些其他逻辑之后, 要让 thread 线程结束.
        Thread.sleep(3000);

        // 这个代码就是在修改前面设定的标志位.
        isQuit = true;
        System.out.println("把 thread 线程终止");
    }
}
