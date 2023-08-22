package Demo;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/22
 * Time: 10:43
 */
public class UDPChatServer {
    final static int sizeofLoad = 1024;//载荷大小
    private static final String PathOfChatRecords = "./src/Demo/ChatRecords.txt";//聊天记录文件路径

    private DatagramSocket datagramSocket;

    public UDPChatServer(int port) throws IOException {
        datagramSocket = new DatagramSocket(port);
        start();
    }

    private void start() throws IOException {
        System.out.println("UDP聊天服务器已经启动!");
        while (true) {
            //接收请求
            DatagramPacket rec = new DatagramPacket(new byte[sizeofLoad], sizeofLoad);
            datagramSocket.receive(rec);
            //如果第一次连接,读取聊天记录(利用探测包进行确认)
            String Test = new String(rec.getData(), 0, rec.getLength());
            if (Test.equals("firstPacketStr")) {
                File fileRecord = new File(PathOfChatRecords);
                if (!fileRecord.exists()) {
                    fileRecord.createNewFile();//创建聊天记录文件
                }
                try (InputStream inputStream = new FileInputStream(fileRecord)) {
                    Scanner fileScanner = new Scanner(inputStream);
                    while (fileScanner.hasNextLine()) {
                        String chat = fileScanner.nextLine();
                        DatagramPacket datagramPacketRecord = new DatagramPacket(chat.getBytes(), 0, chat.getBytes().length,
                                new InetSocketAddress(rec.getAddress(), rec.getPort()));
                        datagramSocket.send(datagramPacketRecord);
                    }
                    String finStr = "finStr";
                    DatagramPacket fin = new DatagramPacket(finStr.getBytes(), 0, finStr.getBytes().length,
                            new InetSocketAddress(rec.getAddress(), rec.getPort()));
                    datagramSocket.send(fin);
                }
                datagramSocket.receive(rec);//接收有内容的信息,避免后面继续使用探测包
            }

            //处理请求
            String resStr = process(rec);

            //返回响应
            DatagramPacket res = new DatagramPacket(resStr.getBytes(), 0, resStr.getBytes().length,
                    new InetSocketAddress(rec.getAddress(), rec.getPort()));
            datagramSocket.send(res);
        }

    }

    private String process(DatagramPacket rec) throws IOException {
        String recStr = new String(rec.getData(), 0, rec.getLength());
        //消息前缀
        File fileRecord = new File(PathOfChatRecords);
        String resStr = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        StringBuilder temp = new StringBuilder(rec.getAddress().toString());
        temp.deleteCharAt(0);
        resStr = format.format(date) + " [" + temp + ":" +
                rec.getPort() + "]: " + recStr;
        //写入消息
        try (OutputStream outputStream = new FileOutputStream(fileRecord, true);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
            PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            printWriter.println(resStr);
            printWriter.flush();
        }
        System.out.println("已写入消息: " + resStr);
        //返回消息
        return resStr;
    }

    public static void main(String[] args) {
        try {
            UDPChatServer udpChatServer = new UDPChatServer(9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
