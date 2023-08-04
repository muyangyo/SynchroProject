package File;

import java.io.*;
import java.util.Scanner;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/4
 * Time:20:50
 */
public class fileContextTest {
    public static void main(String[] args) {

        /*// Writer的使用(和 OutputStream 类似)
        try (Writer writer = new FileWriter("./test.txt")) {
            writer.write("12345\n");
            writer.write("1\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        /*//Reader的使用 (和 InputStream 类似)
        try (Reader reader = new FileReader("./test.txt")) {
            while (true) {
                char[] chars = new char[1024];
                int n = reader.read(chars); // n为 实际读到 的字符个数
                //(最后一次会单独读到一个 -1 表示读取完该文件)

                if (n == -1) {
                    System.out.println("\n该文件已经被读取完");
                    break;
                }
                for (int i = 0; i < n; i++) {
                    System.out.print(chars[i] + " ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        //InputStream的使用
        /*try (InputStream inputStream = new FileInputStream("./test.txt")) {

            while (true) {

                byte[] bytes = new byte[1024];
                int n = inputStream.read(bytes);// n 为 实际读取到 的字节数
                //(最后一次会单独读到一个 -1 表示读取完该文件)
                if (n == -1) {
                    System.out.println("\n读取完成");
                    break;
                }
                for (int i = 0; i < n; i++) {
                    System.out.print(bytes[i] + " ");
                }
                String str = new String(bytes, 0, n);//构造方法自动完成转换
                System.out.println("\n" + str);

            }

        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }*/

       /* //InputStream结合Scanner的使用
        try (InputStream inputStream = new FileInputStream("./test.txt")) {
            Scanner scanner = new Scanner(inputStream, "UTF8");
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


        /*//OutputStream的使用
        try (OutputStream os = new FileOutputStream("./test.txt",true)) {
            String s = "你好中国";
            byte[] b = s.getBytes("utf-8");
            os.write(b);

            // 不要忘记 flush
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

       /* //OutputStream结合PrintWriter的使用
        try (
                OutputStream os = new FileOutputStream("./test.txt");
                OutputStreamWriter osWriter = new OutputStreamWriter(os, "UTF8");
                //告诉它，我们的字符集编码是 utf-8 的
                PrintWriter writer = new PrintWriter(newOutputStreamWriter);
        ) {
            writer.println("第一行");
            writer.print("第二行\n");
            writer.printf("第 %d 行\n", 3);
            writer.flush();// 不要忘记 flush
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

}
