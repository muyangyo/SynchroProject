import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/3
 * Time: 20:19
 */
public class Demo241103 {
    //    public static void main(String[] args) {
//        int a = 1;
//        int b = 3;
//        a = a ^ b;
//        b = b ^ a;
//        a = a ^ b;
//        System.out.println("a = " + a);
//        System.out.println("b = " + b);
//    }

//    public static void main(String[] args) {
//        int a = 10;
//        int b = -20;
//        if ((a ^ b) < 0) {
//            System.out.println("两个变量异号");
//        } else {
//            System.out.println("两个变量同号");
//        }
//
//    }

    public static void main(String[] args) {
        // 定义输入数据
        String data = "123";

        try {
            // 创建MD5哈希对象
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");

            // 更新哈希对象的数据
            md5Digest.update(data.getBytes());

            // 获取哈希值的字节数组
            byte[] hashBytes = md5Digest.digest();

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            // 输出哈希值
            System.out.println("MD5 Hash: " + hexString.toString());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
