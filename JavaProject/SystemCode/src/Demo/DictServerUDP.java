package Demo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/21
 * Time: 21:06
 */
public class DictServerUDP {
    private DatagramSocket socket;

    private HashMap<String, String> dictionary = new HashMap<>();//字典

    public DictServerUDP(int port) throws IOException {
        socket = new DatagramSocket(port);
        createDictionary();//创建字典
        start();
    }

    public void createDictionary() {
        dictionary.put("chinese", "中文");
        dictionary.put("english", "英语");
        dictionary.put("japanese", "日语");
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
        //打印日志
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.print("日志信息 :[" + rec.getAddress() + ":" + rec.getPort() + "][" + format.format(date) + "] ");
        //请求信息
        String recStr = new String(rec.getData(), 0, rec.getLength());
        System.out.print("请求的数据:" + recStr + " ");

        //返回信息
        String resStr = dictionary.getOrDefault(recStr, "未记录该单词");
        System.out.println("返回的数据:" + resStr);
        //String recStr = rec.getData().toString();


        //返回数据
        DatagramPacket res = new DatagramPacket(resStr.getBytes(), 0, resStr.getBytes().length,
                new InetSocketAddress(rec.getAddress(), rec.getPort()));
        return res;
    }

    public static void main(String[] args) {
        try {
            DictServerUDP serverUDP = new DictServerUDP(9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
