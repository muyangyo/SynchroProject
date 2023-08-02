package Mode;

import java.util.PriorityQueue;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/30
 * Time:17:24
 */


//任务体
class myTimerTask implements Comparable<myTimerTask> {
    private Runnable runnable;//可执行任务
    private long time;//延迟时间

    public myTimerTask(Runnable task, long time) {
        this.runnable = task;
        this.time = System.currentTimeMillis() + time;//这样才能知道是否达到执行时间,单 time 做不到
    }

    @Override //优先级队列必须是可比较对象
    public int compareTo(myTimerTask o) {
        return (int) (this.time - o.time); // 底层是 小根堆 ,延迟越小的越靠前
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public long getTime() {
        return time;
    }
}

//定时器
public class myTimer {
    // 使用优先级队列, 来保存上述的 N 个任务
    private volatile PriorityQueue<myTimerTask> queue = new PriorityQueue<>();
    // 用来加锁的对象
    private Object locker = new Object();

    // 定时器的核心方法,就是把要执行的任务添加到队列中
    public void schedule(Runnable runnable, long delay) {
        synchronized (locker) {
            queue.offer(new myTimerTask(runnable, delay));
            locker.notify();
            // 每次来新的任务,都唤醒一下之前的扫描线程.好让扫描线程根据最新的任务情况,重新规划等待时间
        }
    }

    // myTimer 中还需要构造一个 "扫描和执行线程", 一方面去负责监控 队首元素 是否到点了; 一方面当任务到点之后,
    // 就要调用这里的 Runnable 的 Run 方法来完成任务
    public myTimer() {
        Thread thread = new Thread(() -> {
            synchronized (locker) {

                while (true) {

                    try {
                        //为空时等待,等待有新任务进入
                        while (queue.isEmpty()) {
                            locker.wait();
                        }

                        //不为空
                        myTimerTask task = queue.peek();
                        long time = task.getTime();//得到队首的时间(队首时间最短)
                        //到达执行时间
                        if (System.currentTimeMillis() >= time) {
                            task.getRunnable().run();//执行
                            queue.poll();//移除任务
                        }
                        //未到达执行时间,休眠等待(如果有新任务进入则重新唤醒安排睡眠时间)
                        else {
                            //这里不用 while 包裹,就算 异常唤醒 了,还是一样执行到这里进行休眠
                            locker.wait(time - System.currentTimeMillis());
                            //这里为什么不选 sleep ?
                            //因为该线程大部分时间是处于休眠状态的,如果不让出锁的话,其他线程无法插入任务
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

            }

        });
        thread.start();//开启线程
    }

}
