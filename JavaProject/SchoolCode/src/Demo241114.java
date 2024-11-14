import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/14
 * Time: 17:22
 */
public class Demo241114 {
    public static void main1(String[] args) {
        // 定义整数类型变量
        int myInt = 42;

        // 定义浮点数类型变量
        float myFloat = 3.14f;
        double myDouble = 2.71828;

        // 定义布尔类型变量
        boolean myBoolean = true;

        // 定义字符类型变量
        char myChar = 'A';

        // 定义字符串类型变量
        String myString = "Hello, World!";

        // 打印所有变量的值
        System.out.println("整数 (int): " + myInt);
        System.out.println("浮点数 (float): " + myFloat);
        System.out.println("浮点数 (double): " + myDouble);
        System.out.println("布尔值 (boolean): " + myBoolean);
        System.out.println("字符 (char): " + myChar);
        System.out.println("字符串 (String): " + myString);
    }

    public static void main2(String[] args) {
        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }

    public static void main3(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入第一个数字: ");
        double a = scanner.nextDouble();
        System.out.print("请输入第二个数字: ");
        double b = scanner.nextDouble();
        System.out.print("请输入第三个数字: ");
        double c = scanner.nextDouble();

        if (a + b > c && a + c > b && b + c > a) {
            System.out.println("这三个数字可以构成一个三角形。");
        } else {
            System.out.println("这三个数字不能构成一个三角形。");
        }
    }

    public static double[] calculate(double num1, double num2) {
        double sum = num1 + num2;
        double difference = num1 - num2;
        double product = num1 * num2;
        double quotient = num2 != 0 ? num1 / num2 : Double.NaN;

        return new double[]{sum, difference, product, quotient};
    }

    public static void main(String[] args) {
        double num1 = 10;
        double num2 = 5;

        double[] results = calculate(num1, num2);

        System.out.println("和: " + results[0]);
        System.out.println("差: " + results[1]);
        System.out.println("乘积: " + results[2]);
        System.out.println("商: " + results[3]);
    }
}
