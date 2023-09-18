package NetWork;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoClient {
    private Socket socket = null;


    public TcpEchoClient(String serverIp, int serverPort) throws IOException {
        //这个 new 操作完成之后, 就完成了 tcp 连接的建立.(有服务器)
        socket = new Socket(serverIp, serverPort);
        start();
    }

    public void start() {
        System.out.println("客户端启动");

        Scanner structure = new Scanner(System.in);//用于构造请求的 Scanner

        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            while (true) {
                // 1. 构造请求
                System.out.print("-> ");
                String request = structure.next();

                // 2. 发送请求
                PrintWriter printWriter = new PrintWriter(outputStream);
                //    使用 println 带上换行. 后续服务器读取请求, 就可以使用 scanner.next 来获取了
                printWriter.println(request);
                printWriter.flush();

                // 3. 处理响应
                Scanner scannerResponse = new Scanner(inputStream);//用于读取响应的
                String response = scannerResponse.next();

                // 4. 把响应打印出来(可不要)
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        TcpEchoClient client = new TcpEchoClient("127.0.0.1", 9090);
    }
}
