package NetWork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

// UDP 的 回显服务器 : 客户端发的请求是啥, 服务器返回的响应就是啥
public class UdpEchoServer {
    private DatagramSocket socket = null;

    // 参数是服务器要绑定的端口
    public UdpEchoServer(int port) throws IOException {
        socket = new DatagramSocket(port);
        start();//启动
    }

    // 使用这个方法启动服务器
    public void start() throws IOException {
        System.out.println("服务器启动!");
        // 反复的, 长期的执行针对客户端请求处理的逻辑.
        while (true) {
            // 一个服务器, 运行过程中, 要做的事情, 主要是三个核心环节

            // 1. 读取请求
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096], 4096);// 先设计一个包最大多少
            socket.receive(requestPacket);//接收数据包(如果 ＞ 4096 则截断, ＜ 则 丢弃后面的多余的空间,但是数据包的大小是不变的(容器不变))

            // 2. 处理请求
            // 这样的转字符串的前提是, 后续客户端发的数据就是一个文本的字符串
            String request = new String(requestPacket.getData(), 0, requestPacket.getLength());
            String response = process(request);

            // 3. 返回响应
            // 此时需要告知网卡, 要发的内容是啥, 要发给谁.
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), 0, response.getBytes().length,
                    requestPacket.getSocketAddress());
            socket.send(responsePacket);

            // 记录日志, 方便观察程序执行效果(可不要)
            System.out.printf("[%s:%d] req: %s, resp: %s\n", requestPacket.getAddress().toString(), requestPacket.getPort(),
                    request, response);
        }
    }

    // 对请求进行加工
    public String process(String request) {
        return request;
    }


    public static void main(String[] args) throws IOException {
        UdpEchoServer server = new UdpEchoServer(9090);
    }

}
