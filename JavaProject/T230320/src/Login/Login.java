package Login;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/28
 * Time: 21:32
 */

public class Login extends Exception {
    private final String UserName = "admin";
    private final String UserPassword = "123456";

    public void LoginStart(String Name, String Password) throws Exception {
        if (!Name.equals(UserName)) {
            throw new Exception("用户名错误");
        } else if (!Password.equals(UserPassword)) {
            throw new Exception("密码错误");
        } else
            System.out.println("登录成功!");
    }
}

