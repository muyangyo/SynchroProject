import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class HeartbeatClient {
    private final String serverIp;
    private final int port;
    private final String deviceId;
    private volatile boolean running = true;

    public HeartbeatClient(String serverIp, int port, String deviceId) {
        this.serverIp = serverIp;
        this.port = port;
        this.deviceId = deviceId;
    }

    public void start() {
        new Thread(() -> {
            while (running) {
                try (Socket socket = new Socket(serverIp, port)) {
                    OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                    out.write("HEARTBEAT:" + deviceId + "\n");
                    out.flush();
                } catch (Exception e) {
                    System.err.println("Heartbeat failed: " + e.getMessage());
                }
                try {
                    TimeUnit.SECONDS.sleep(30); // 每30秒发送一次
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void stop() {
        running = false;
    }
}