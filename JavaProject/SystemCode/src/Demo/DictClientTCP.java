package Demo;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/8/21
 * Time: 22:05
 */
public class DictClientTCP {
    private Socket socket;

    public DictClientTCP(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        start();
    }

    private void start() throws IOException {
        System.out.println("客户端已启动");

        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
            Scanner scanner = new Scanner(System.in);//读取控制台的信息
            Scanner inputStreamScanner = new Scanner(inputStream);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            while (true) {
                //构造请求
                System.out.print("请输入英文单词:");
                String rep = scanner.next();

                //发送请求
                printWriter.println(rep);
                printWriter.flush();

                //处理响应
                process(inputStreamScanner);
            }
        } finally {
            socket.close();
        }
    }

    private void process(Scanner scanner) {
        System.out.println("中文:" + scanner.next());
    }

    public static void main(String[] args) {
        try {
            DictClientTCP dictClientTCP = new DictClientTCP("127.0.0.1", 9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
