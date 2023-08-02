package Mode;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/30
 * Time:17:11
 */
public class Demo230730 {

    public static void main(String[] args) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task-1500");
            }
        }, 1500);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task-1000");
            }
        }, 1000);




    }
}
