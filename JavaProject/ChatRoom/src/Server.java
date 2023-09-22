import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/22
 * Time: 19:34
 */
public class Server {
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private List<Socket> socketList = new LinkedList<>();//记录socket对象
    private final FileControl fileControl;//负责对文件进行操作


    public Server(int port, String path) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.fileControl = new FileControl(path);
        start();
    }

    //负责启动线程接管socket对象
    private void start() throws IOException {
        System.out.println("服务器已启动!");
        while (true) {
            Socket socket = serverSocket.accept();
            threadPool.submit(() -> {
                try {
                    process(socket);
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            socketList.add(socket);
        }
    }


    //处理连接后的事务
    private void process(Socket socket) {
        try (InputStream inputStream = socket.getInputStream(); OutputStream outputStream = socket.getOutputStream();
             Scanner inScanner = new Scanner(inputStream, "UTF8");
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF8");
             PrintWriter printWriter = new PrintWriter(outputStreamWriter);
        ) {
            User user = new User(socket.getPort());
            //广播上线信息
            update(printWriter, "======" + user.id + "号客户端已上线!======", socket);
            //单独发送旧数据
            readAll(printWriter, user);
            //三步走战略
            while (true) {
                if (!inScanner.hasNextLine()) {
                    //广播下线信息
                    update(printWriter, "======" + user.id + "号客户端以下线!=====", socket);
                    break;//结束通信
                }
                //接收请求
                String req = receive(inScanner);

                //处理请求
                String resp = user.id + "客户端: " + req;
                update(printWriter, resp, socket);

                //返回响应
                //这里为了美观用不到(前面也发送了请求)
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //更新聊天记录文件,并且对使用socket连接发送新信息
    private void update(PrintWriter writer, String str, Socket socket) throws IOException {
        fileControl.write(str);
        for (Socket temp : socketList) {
            if (temp.equals(socket))
                continue;
            else
                sent(writer, str);
        }
    }

    private void readAll(PrintWriter writer, User user) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(fileControl.file);
             Scanner fileScanner = new Scanner(fileInputStream, "UTF8")) {
            sent(writer, user.id + "");
            while (fileScanner.hasNextLine()) {
                sent(writer, fileScanner.nextLine());
            }
            sent(writer, "EOF");
        }
    }

    private void sent(PrintWriter writer, String resp) {
        writer.println(resp);
        writer.flush();
    }

    private String receive(Scanner scanner) {
        return scanner.nextLine();
    }

    //清理聊天记录
    public boolean clear() throws IOException {
        return fileControl.clear();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(9090, "./src/Demo.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
