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
 * Date: 2023/8/22
 * Time: 11:30
 */
public class UDPChatClient {
    private DatagramSocket datagramSocket;
    private String ip;
    private int port;

    public UDPChatClient(String Ip, int Port) throws IOException {
        this.datagramSocket = new DatagramSocket();
        ip = Ip;
        port = Port;
        start();
    }

    private void start() throws IOException {
        //构造和发送探测包
        String firstPacketStr = "firstPacketStr";
        DatagramPacket firstPacket = new DatagramPacket(firstPacketStr.getBytes(), 0, firstPacketStr.getBytes().length,
                new InetSocketAddress(ip, port));
        datagramSocket.send(firstPacket);
        //接受聊天记录
        while (true) {
            DatagramPacket recPacket = new DatagramPacket(new byte[UDPChatServer.sizeofLoad], UDPChatServer.sizeofLoad);
            datagramSocket.receive(recPacket);
            String recStr = new String(recPacket.getData(), 0, recPacket.getLength());
            if (recStr.equals("finStr")) {
                break;
            }
            System.out.println(recStr);
        }
        while (true) {
            //构造请求
            Scanner scanner = new Scanner(System.in);
            System.out.print("按回车发送消息: ");
            String sendStr = scanner.nextLine();
            //发送请求
            DatagramPacket sendPacket = new DatagramPacket(sendStr.getBytes(), 0, sendStr.getBytes().length,
                    new InetSocketAddress(ip, port));
            datagramSocket.send(sendPacket);
            //处理响应
            DatagramPacket recPacket = new DatagramPacket(new byte[UDPChatServer.sizeofLoad], UDPChatServer.sizeofLoad);
            datagramSocket.receive(recPacket);
            String recStr = new String(recPacket.getData(), 0, recPacket.getLength());
            System.out.println(recStr);
        }
    }

    public static void main(String[] args) {
        try {
            UDPChatClient udpChatClient = new UDPChatClient("127.0.0.1", 9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
