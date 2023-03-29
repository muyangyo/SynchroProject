package Login;

import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名:");
        String name = sc.nextLine();
        System.out.println("请输入密码:");
        String password = sc.nextLine();
        Login login = new Login();
        try {
            login.LoginStart(name, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main1(String[] args) {
        try {
            throw new NullPointerException("空指针异常");
        } catch (NullPointerException | ArithmeticException e1) {
            e1.printStackTrace();
            System.out.println("已成功处理异常!");
        }
    }

}