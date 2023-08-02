package Mode;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/29
 * Time:17:55
 */

public class myBlockingQueue<T> {
    private T[] queue = (T[]) new Object[100];//循环队列
    private volatile int head = 0;//头
    private volatile int tail = 0;//尾
    private volatile int usedSize = 0;//元素个数

    //入队列
    public void put(T x) throws InterruptedException {

        // 推荐写成这样而不是加到函数名上,这样后面更容易知道锁是哪个对象
        synchronized (this) {
            while (usedSize == queue.length) {
                //队列满,阻塞处于 put 的进程
                this.wait();
            }
            queue[tail] = x;
            tail = (tail + 1) % queue.length;
            usedSize++;
            //唤醒处于 take 的线程,如果有阻塞就唤醒,没有则无视
            this.notify();
        }

    }

    //出队列
    public T take() throws InterruptedException {
        synchronized (this) {
            while (usedSize == 0) {
                //队列为空的情况,阻塞处于 take 的进程
                this.wait();
            }
            T ret = queue[head];
            head = (head + 1) % queue.length;
            usedSize--;
            this.notify();
            return ret;
        }
    }
}
