package NetWork;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UdpEchoClient {
    private DatagramSocket socket = null;
    private String serverIp;
    private int serverPort;

    // 服务器的 ip 和 服务器的端口
    public UdpEchoClient(String ip, int port) throws IOException {
        serverIp = ip;
        serverPort = port;
        // 这个 new 操作, 就不再指定端口了. 让系统自动分配一个空闲端口即可
        socket = new DatagramSocket();
        start();//启动
    }

    // 让这个客户端反复的从控制台读取用户输入的内容. 把这个内容构造成 UDP 请求, 发给服务器. 再读取服务器返回的 UDP 响应
    // 最终再显示在客户端的屏幕上.
    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("客户端启动!");

        while (true) {
            // 1. 构造请求
            System.out.print("-> "); // 命令提示符, 提示用户要输入字符串.
            String request = scanner.next();
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(), 0, request.getBytes().length,
                    new InetSocketAddress(this.serverIp, this.serverPort));

            // 2.发生请求
            socket.send(requestPacket);

            // 3.处理响应
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096], 4096);
            socket.receive(responsePacket);
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());

            // 显示到屏幕上(可不要)
            System.out.println(response);
        }
    }

    public static void main(String[] args) throws IOException {
        UdpEchoClient client = new UdpEchoClient("127.0.0.1", 9090);
    }
}
