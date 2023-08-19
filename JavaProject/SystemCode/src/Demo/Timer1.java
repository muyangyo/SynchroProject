package Demo;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/19
 * Time: 11:06
 */
class Task implements Comparable<Task> {
    Runnable Task;
    Long Time;

    Task(Runnable t, int time) {
        this.Task = t;
        this.Time = System.currentTimeMillis() + time;
    }

    @Override
    public int compareTo(Demo.Task o) {
        return (int) (this.Time - o.Time);
    }
}

public class Timer1 {
    Queue<Task> queue = new PriorityQueue<>();
    Object locker = new Object();

    public Timer1() {
        Thread thread = new Thread(() -> {
            while (true) {
                synchronized (locker) {
                    try {
                        while (queue.isEmpty()) {
                            locker.wait();//放锁等待
                        }
                        Long time = queue.peek().Time;
                        if (System.currentTimeMillis() >= time) {
                            queue.poll().Task.run();
                        } else {
                            locker.wait(time - System.currentTimeMillis());
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
    }

    public void schedule(Runnable runnable, int time) {
        synchronized (locker) {
            Task t = new Task(runnable, time);
            queue.offer(t);
            locker.notify();
        }
    }

    public static void main(String[] args) {
        Timer1 timer = new Timer1();
        timer.schedule(() -> {
            System.out.println("1000");
        }, 1000);
        timer.schedule(() -> {
            System.out.println("1100");
        }, 1100);
        timer.schedule(() -> {
            System.out.println("1200");
        }, 1200);
        timer.schedule(() -> {
            System.out.println("500");
        }, 500);
    }
}
