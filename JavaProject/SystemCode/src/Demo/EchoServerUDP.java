package Demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/21
 * Time: 15:12
 */
public class EchoServerUDP {
    private DatagramSocket socket;

    public EchoServerUDP(int port) throws IOException {
        socket = new DatagramSocket(port);
        start();
    }

    private void start() throws IOException {
        System.out.println("服务端已启动!");
        while (true) {
            //接收数据
            DatagramPacket rec = new DatagramPacket(new byte[1024], 1024);
            socket.receive(rec);

            //处理数据
            DatagramPacket res = process(rec);

            //返回数据
            socket.send(res);
        }
    }

    private DatagramPacket process(DatagramPacket rec) {
        //打印信息
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.print("日志信息 :[" + rec.getAddress() + ":" + rec.getPort() + "][" + format.format(date) + "] ");
        String inf = new String(rec.getData(), 0, rec.getLength());
        System.out.println(inf);

        //String inf = rec.getData().toString();

        //返回数据
        DatagramPacket res = new DatagramPacket(inf.getBytes(), 0, inf.getBytes().length,
                new InetSocketAddress(rec.getAddress(), rec.getPort()));
        return res;
    }

    public static void main(String[] args) {
        try {
            EchoServerUDP serverUDP = new EchoServerUDP(9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
