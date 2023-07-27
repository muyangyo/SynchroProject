package Mode;


import java.util.concurrent.*;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/27
 * Time:21:00
 */
public class TestDemo230727 {

    public static void main(String[] args) throws InterruptedException {
        //阻塞队列的使用
        BlockingQueue<String> exp = new LinkedBlockingQueue<>();
        // BlockingQueue<String> exp = new ArrayBlockingQueue<>(10);
        // BlockingQueue<String> exp = new PriorityBlockingQueue<>();

        //带阻塞功能的操作
        exp.put("hello");
        String tmp = exp.take();
        System.out.println(tmp);

        //不带阻塞功能的 原队列 功能(不推荐使用)
        /*exp.offer("hello");
        exp.add("hello");
        exp.poll();
        exp.peek();*/
    }

}
