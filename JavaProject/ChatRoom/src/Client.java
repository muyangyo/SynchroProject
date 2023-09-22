import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/22
 * Time: 19:34
 */
public class Client {
    private Socket socket = null;

    public Client(String serverIp, int serverPort) throws IOException {
        socket = new Socket(serverIp, serverPort);
        start();
    }

    public void start() {
        System.out.println("客户端已启动!");

        Scanner structure = new Scanner(System.in);//用于构造请求的 Scanner

        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             Scanner scannerResponse = new Scanner(inputStream)//用于读取响应的
        ) {
            //接收聊天记录
            String str = scannerResponse.nextLine();
            String userId = str;
            while (!str.equals("EOF")) {
                System.out.println(str);
                str = scannerResponse.nextLine();
            }
            System.out.println("============旧记录============");
            Thread thread = new Thread(() -> {
                    // 1. 构造请求
                    System.out.print(userId + "客户端:");
                    String request = structure.nextLine();

                    // 2. 发送请求
                    PrintWriter printWriter = new PrintWriter(outputStream);
                    printWriter.println(request);
                    printWriter.flush();

            });
            thread.start();

            while (true) {
                // 3. 处理响应
                while (scannerResponse.hasNextLine()) {
                    String response = scannerResponse.nextLine();
                    System.out.println(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1", 9090);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


