package Demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/21
 * Time: 21:07
 */
public class DictClientUDP {
    private DatagramSocket socket;
    private String IP;
    private int port;

    public DictClientUDP(String ip, int port) throws IOException {
        this.IP = ip;
        this.port = port;
        socket = new DatagramSocket();
        start();
    }

    private void start() throws IOException {

        System.out.println("客户端已启动!");
        while (true) {
            //构造请求
            Scanner sc = new Scanner(System.in);
            System.out.print("输入英语单词:");
            String sendStr = sc.next();
            DatagramPacket send = new DatagramPacket(sendStr.getBytes(), 0, sendStr.getBytes().length,
                    new InetSocketAddress(IP, port));

            //发送请求
            socket.send(send);

            //处理响应
            DatagramPacket res = new DatagramPacket(new byte[1024], 1024);
            socket.receive(res);
            process(res);
        }

    }

    private void process(DatagramPacket res) {
        String resStr = new String(res.getData(), 0, res.getLength());
        System.out.println("中文:" + resStr);
    }

    public static void main(String[] args) throws IOException {
        DictClientUDP serverUDP = new DictClientUDP("127.0.0.1", 9090);
    }
}
