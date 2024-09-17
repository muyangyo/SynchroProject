package Base;

public class Sleep {
    /**
     * 使当前线程暂停执行3000毫秒（3秒）
     */
    public void sleepLongTime() throws InterruptedException {
        Thread.sleep(3000);
    }

    /**
     * 使当前线程暂停执行1500毫秒（1.5秒）
     */
    public void sleepNormalTime() throws InterruptedException {
        Thread.sleep(1500);
    }

    /**
     * 使当前线程暂停执行500毫秒（0.5秒）
     */
    public void sleepShortTime() throws InterruptedException {
        Thread.sleep(500);
    }
}