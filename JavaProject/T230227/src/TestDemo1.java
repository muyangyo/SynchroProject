import java.util.Arrays;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/2/27
 * Time: 20:08
 */
public class TestDemo1 {
    public static void NumB(int x) {
        int mount = 0;
        int i = 0;
        while (x != 0) {
            if ((x & 1) == 1)
                mount++;
            x >>>= 1;
        }
        System.out.println("该数字有 " + mount + " 个二进制'1'");
    }

    public static boolean IsPrimeNumber(int x) {
        for (int i = 2; i <= Math.sqrt(x); i++) {
            if (x % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean IsLeapYear(int x) {
        if (x % 4 == 0 && x % 100 != 0) {
            return true;
        } else if (x % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void main1(String[] args) {
        int flag = 0;
        for (int i = 1000; i <= 2000; i++) {
            if (IsLeapYear(i) == true) {
                flag++;
                System.out.print(i + " ");
                if (flag % 10 == 0) {
                    System.out.println();
                }
            }
        }
    }

    public static void NumberTimes(int x) {
        int count = 0;
        for (int i = 1; i < 100; i++) {
            if (i / 10 == x) {
                count++;
            } else if (i % 10 == x) {
                count++;
            }
        }
        System.out.println(count);
    }

    public static void main2(String[] args) {
        NumberTimes(1);
    }

    public static void MultiplicationTable() {
        for (int h = 1; h < 10; h++) {
            for (int l = 1; l <= h; l++) {
                System.out.print("" + h + " * " + l + " = " + (h * l) + "  ");
            }
            System.out.println();
        }

    }

    public static void PrintENum(int x) {
        while (x != 0) {
            System.out.println(x % 10);
            x /= 10;
        }
    }

    public static void Login() {
        int flag = 0;
        System.out.println("请输入密码:");
        String x;
        Scanner sc = new Scanner(System.in);
        while (flag < 3) {
            x = sc.nextLine();
            if (x.equals("123456")) {
                System.out.println("登录成功");
                return;
            } else {
                flag++;
                System.out.println("密码错误,请重新输入密码");
            }
        }
        System.out.println("三次机会已经用完,自动退出程序");
    }

    public static void DtoB(int x) {
        if (x != 0) {
            DtoB(x / 2);
            System.out.print(x % 2);
        }
    }

    public static void DtoBAll(int x) {
        int[] ED = new int[16];
        int posE = 0;
        int[] OD = new int[16];
        int posD = 0;
        for (int i = 0; i < 32; i++) {
            if (i % 2 == 0) {
                OD[posD] = x & 1;
                posD++;
            } else {
                ED[posE] = x & 1;
                posE++;
            }
            x = x >>> 1;
        }
        System.out.print("奇数位: ");
        for (int i = 15; i >= 0; i--) {
            System.out.print(ED[i] + " ");
        }
        System.out.println();
        System.out.print("偶数位: ");
        for (int i = 15; i >= 0; i--) {
            System.out.print(OD[i] + " ");
        }
        System.out.println();

    }

    public static int add(int x, int y) {
        return x + y;
    }


    public static void Sum() {
        double sum = 0;
        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                sum -= 1.0 / i;
            } else {
                sum += 1.0 / i;
            }
        }
        System.out.println(sum);
    }


    public static void PrintX(int x) {
        char[] chars = new char[20];
        //初始化
        for (int i = 0; i < 20; i++) {
            chars[i] = ' ';
        }
        //打印
        for (int i = 0; i < x; i++) {
            char[] temp = chars.clone();
            temp[i] = '*';
            temp[x - i - 1] = '*';
            for (int j = 0; j < x; j++) {
                System.out.print(temp[j]);
            }
            System.out.println();
        }
    }

    public static int Max1(int x, int y) {
        return x > y ? x : y;
    }

    public static int Max1(int x, int y, int z) {
        return Max(Max(x, y), z);
    }

    public static int NJ(int x) {
        int sum = 0;
        if (x == 0)
            return 0;
        for (int i = 1; i <= x; i++) {
            int temp = 1;
            for (int j = 1; j <= i; j++) {
                temp *= j;
            }
            sum += temp;
        }
        return sum;
    }

    public static long Fib(int x) {
        //1 1 2 3 5 8
        //a b
        long a = 1;
        long b = 1;
        long sum = 0;
        if (x == 0) {
            return 0;
        }
        if (x < 2) {
            return 1;
        }
        for (int i = 2; i < x; i++) {
            sum = a + b;
            a = b;
            b = sum;

        }
        return sum;
    }

    public static int Add(int x, int y) {
        return x + y;
    }

    public static Double Add(double x, double y, double z) {
        return x + y + z;
    }

    public static int Max(int x, int y) {
        return Math.max(x, y);
    }

    public static int Max(int x, int y, int z) {
        return Math.max(Math.max(x, y), z);
    }


    public static void main(String[] args) {


    }
}
