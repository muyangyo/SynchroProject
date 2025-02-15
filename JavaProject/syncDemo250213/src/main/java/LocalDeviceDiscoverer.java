import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class LocalDeviceDiscoverer {
    public static List<String> discoverDevices() {
        List<String> devices = new ArrayList<>();
        try (DatagramSocket socket = new DatagramSocket()) { // 创建 UDP 客户端
            socket.setBroadcast(true); // 允许发送广播数据
            byte[] requestData = "DISCOVER_DEVICE_REQUEST".getBytes(); // 请求数据
            DatagramPacket requestPacket = new DatagramPacket(
                    requestData,
                    requestData.length,
                    InetAddress.getByName("255.255.255.255"), // 广播地址
                    8888
            ); // 创建请求数据包
            socket.send(requestPacket); // 发送请求数据包

            // 等待响应（2秒）
            socket.setSoTimeout(2000);
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(responsePacket);
                    String response = new String(responsePacket.getData()).trim();
                    if (response.startsWith("DISCOVER_DEVICE_RESPONSE:")) {
                        String deviceId = response.split(":")[1];
                        devices.add(deviceId + "@" + responsePacket.getAddress().getHostAddress());
                    }
                } catch (java.net.SocketTimeoutException e) {
                    System.out.println("Timeout: No more responses.");
                    break; // 超时则退出循环
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices;
    }

    public static void main(String[] args) {
        List<String> strings = discoverDevices();
        System.out.println(strings);
    }
}