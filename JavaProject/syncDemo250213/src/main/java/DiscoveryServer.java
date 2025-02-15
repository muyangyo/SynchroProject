import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DiscoveryServer implements Runnable {
    private static final int UDP_PORT = 8888;
    private final String deviceId;

    public DiscoveryServer(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(UDP_PORT)) {
//            socket.setBroadcast(true); // 开启广播模式
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // 接收客户端广播
                String message = new String(packet.getData()).trim();
                if ("DISCOVER_DEVICE_REQUEST".equals(message)) {
                    // 回复设备信息
                    String response = "DISCOVER_DEVICE_RESPONSE:" + deviceId; // 回复消息格式为：DISCOVER_DEVICE_RESPONSE:设备ID
                    byte[] responseData = response.getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(
                            responseData,
                            responseData.length,
                            packet.getAddress(),
                            packet.getPort()
                    );
                    socket.send(responsePacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
  /*      String s = DeviceIdGenerator.generateDeviceId();
        System.out.println("Device ID: " + s);
        new DiscoveryServer(s).run();*/
    }
}