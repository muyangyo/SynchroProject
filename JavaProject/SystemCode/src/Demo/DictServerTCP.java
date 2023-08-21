package Demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/21
 * Time: 22:04
 */
public class DictServerTCP {
    private ServerSocket serverSocket;

    private HashMap<String, String> dictionary = new HashMap<>();//字典

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public DictServerTCP(int port, int n) throws IOException {
        serverSocket = new ServerSocket(port);
        createDictionary();
        start(n);
    }

    private void createDictionary() {
        dictionary.put("chinese", "中文");
        dictionary.put("english", "英语");
        dictionary.put("japanese", "日语");
    }


    private void start(int n) throws IOException {
        System.out.println("服务器已启动!");

        while (true) {
            Socket socket = serverSocket.accept();
            cachedThreadPool.submit(() -> {
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
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private String process(Scanner scanner, Socket socket) {
        //打印日志
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.print("日志信息 :[" + socket.getInetAddress() + ":" + socket.getPort() + "][" + format.format(date) + "] ");

        String recStr = scanner.next();
        System.out.print("请求的信息:" + recStr + " ");

        String resStr = dictionary.getOrDefault(recStr, "未记录该单词!");
        System.out.println("返回的信息:" + resStr);
        return resStr;
    }

    public static void main(String[] args) {
        try {
            DictServerTCP dictServerTCP = new DictServerTCP(9090, 10);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
