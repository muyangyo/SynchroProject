package File;

import java.io.*;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/8/2
 * Time:16:56
 */
public class fileDemo230802 {
    public static void main(String[] args) {
        try (Reader reader = new FileReader("123")){
            ;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
