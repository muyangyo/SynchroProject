package Demo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/11
 * Time: 23:41
 */
public class Demo231011 {
    /*public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(10024);
            Socket socket = serverSocket.accept();
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream();
                 Scanner scanner = new Scanner(inputStream, "UTF8");
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF8");
                 PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            ) {
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
    public static void main(String[] args) {
        //这里以Tomcat作为服务器
        String ip = "127.0.0.1";
        try {
            Socket socket = new Socket(ip, 8080);
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream();
                 Scanner scanner = new Scanner(inputStream, "UTF8");
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF8");
                 PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            ) {
                String req = "GET http://localhost:8080/Servlet230916/1.html HTTP/1.1\n" + "Host: localhost:8080\n";
                printWriter.println(req);
                printWriter.flush();
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
