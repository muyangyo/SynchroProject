package NetWork;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpEchoServer {
    private ServerSocket serverSocket = null;

    private ExecutorService service = Executors.newCachedThreadPool();

    // 设置 服务器端 的端口号
    public TcpEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        start();
    }

    // 启动服务器
    public void start() throws IOException {
        System.out.println("服务器启动!");
        while (true) {
            Socket clientSocket = serverSocket.accept();//带阻塞功能,建立连接

            /*
        单个线程,实现不了这里的一边拉客,一边介绍(会导致后面的客人来不了)
        就需要多线程. 主线程专门负责拉客. 每次有一个客户端, 都创建一个新的线程去服务
        但是此处 不应该创建固定线程数目 的线程池/线程(避免重复的创建和关闭线程的开销)
            Thread t = new Thread(() -> {
                processConnection(clientSocket);
            });
            t.start();
         */

            // 使用线程池, 来解决上述问题
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        processConnection(clientSocket);
                        clientSocket.close();//关闭当前连接
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }
    }

    // 通过这个方法来处理一个连接后的逻辑
    private void processConnection(Socket clientSocket) throws IOException {
        System.out.printf("[%s:%d] 客户端上线!\n", clientSocket.getInetAddress().toString(), clientSocket.getPort());
        // 接下来就可以读取请求, 根据请求计算响应, 返回响应三步走了.
        // Socket 对象内部包含了两个字节流对象, 把这俩字节流对象获取到, 完成后续的读写工作
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {
            // 一次连接中, 可能会涉及到多次请求/响应
            while (true) {
                // 1. 收到请求. 为了读取方便, 直接使用 Scanner.
                Scanner scanner = new Scanner(inputStream);
                if (!scanner.hasNext()) {
                    // 读取完毕, 客户端下线.
                    System.out.printf("[%s:%d] 客户端下线!\n", clientSocket.getInetAddress().toString(),
                            clientSocket.getPort());
                    break;
                }
                // 这个代码暗含一个前提:客户端发过来的请求得是文本数据,同时还得带有空白符作为分割. (比如换行这种)
                String request = scanner.next();

                // 2. 处理请求
                String response = process(request);

                // 3. 把响应写回给客户端. 把 OutputStream 使用 PrinterWriter 包裹一下, 方便进行发数据.
                PrintWriter writer = new PrintWriter(outputStream);
                //    使用 println 带上换行. 后续客户端读取请求, 就可以使用 scanner.next 来获取了
                writer.println(response);
                writer.flush();

                // 日志, 打印当前的请求详情(可不要)
                System.out.printf("[%s:%d] req: %s, resp: %s\n", clientSocket.getInetAddress().toString(),
                        clientSocket.getPort(),
                        request, response);
            }

        }

    }

    public String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoServer server = new TcpEchoServer(9090);
    }
}
