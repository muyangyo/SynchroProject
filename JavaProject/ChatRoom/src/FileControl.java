import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/22
 * Time: 19:51
 */
public class FileControl {
    public File file;
    private Object locker = new Object();

    public FileControl(String path) throws IOException {
        this.file = new File(path);
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("文件创建失败!");
            }
        }
    }

    public void write(String str) throws IOException {
        synchronized (locker) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                 OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                 PrintWriter printWriter = new PrintWriter(outputStreamWriter)) {
                printWriter.println(str);
                System.out.println("已记录聊天信息!");
                printWriter.flush();
            }
        }
    }

    public boolean clear() throws IOException {
        synchronized (locker) {
            if (this.file.delete()) {
                return this.file.createNewFile();
            } else return false;
        }
    }

    //仅测试用
    /*public static void main(String[] args) {
        try {
            FileControl fileControl = new FileControl("./src/logs.txt");
            fileControl.write("Demo230922");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
}
