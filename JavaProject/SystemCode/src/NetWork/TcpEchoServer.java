package NetWork;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpEchoServer {
    private ServerSocket serverSocket = null;

    private ExecutorService service = Executors.newCachedThreadPool();//自动调整的线程池

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
            不应该创建固定线程数目的线程池/线程(避免重复的创建和关闭线程的开销)
            Thread t = new Thread(() -> {
                processConnection(clientSocket);
            });//这是是lambda表达式实现的
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
            });//线程池有任务则会自动分配线程执行,所以只需要submit即可

        }
    }

    // 通过这个方法来处理一个连接后的逻辑
    private void processConnection(Socket clientSocket) throws IOException {
        System.out.printf("[%s:%d] 客户端上线!\n", clientSocket.getInetAddress().toString(), clientSocket.getPort());
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
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF8");//规定编码格式
                PrintWriter writer = new PrintWriter(outputStreamWriter);
                writer.println(response);
                writer.flush();

                // 日志, 打印当前的请求详情(可不要)
                System.out.printf("[%s:%d] req: %s, resp: %s\n", clientSocket.getInetAddress().toString(),
                        clientSocket.getPort(),
                        request, response);
            }

        }

    }

    //处理请求部分
    public String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoServer server = new TcpEchoServer(9090);
    }
}
