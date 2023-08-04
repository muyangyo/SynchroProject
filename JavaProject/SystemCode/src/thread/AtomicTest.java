package thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/4
 * Time:12:21
 */
public class AtomicTest {
    public static void main(String[] args) {

        AtomicInteger A = new AtomicInteger(1);
        A.incrementAndGet();//++A
        A.getAndIncrement();//A++
        //A++;
        System.out.println(A);

    }
}
