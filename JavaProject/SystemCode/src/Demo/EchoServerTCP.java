package Demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/21
 * Time: 21:29
 */
public class EchoServerTCP {
    private ServerSocket serverSocket;

    public EchoServerTCP(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        start();
    }


    private void start() throws IOException {
        System.out.println("服务器已启动!");
        while (true) {
            //建立链接
            Socket socket = serverSocket.accept();
            System.out.println("客户端[" + socket.getInetAddress() + ":" + socket.getPort() + "] 已上线");
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()) {
                Scanner scanner = new Scanner(inputStream);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                PrintWriter printWriter = new PrintWriter(outputStreamWriter);
                //接收请求
                while (scanner.hasNext()) {
                    //处理请求
                    String resStr = process(scanner, socket);
                    //返回响应
                    printWriter.println(resStr);
                    printWriter.flush();
                }
                System.out.println("客户端[" + socket.getInetAddress() + ":" + socket.getPort() + "] 已下线");
            }
        }
    }

    private String process(Scanner scanner, Socket socket) {
        //打印日志
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.print("日志信息 :[" + socket.getInetAddress() + ":" + socket.getPort() + "][" + format.format(date) + "] ");

        String recStr = scanner.next();
        System.out.print("请求的信息:" + recStr + " ");

        String resStr = recStr;
        System.out.println("返回的信息:" + resStr);
        return resStr;
    }

    public static void main(String[] args) {
        try {
            EchoServerTCP echoServerTCP = new EchoServerTCP(9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
