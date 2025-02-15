import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class HeartbeatServer {
    private static final int TCP_PORT = 8889;
    private static final ConcurrentHashMap<String, Long> onlineDevices = new ConcurrentHashMap<>();

    public static void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message = in.readLine();
            if (message.startsWith("HEARTBEAT:")) {
                String deviceId = message.split(":")[1];
                onlineDevices.put(deviceId, System.currentTimeMillis());
                System.out.println("Device " + deviceId + " is online");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 清理超时设备（定期调用）
    public static void cleanupOfflineDevices(long timeoutMs) {
        long now = System.currentTimeMillis();
        onlineDevices.entrySet().removeIf(entry -> (now - entry.getValue()) > timeoutMs);
    }
}