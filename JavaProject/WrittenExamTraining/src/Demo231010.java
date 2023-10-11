import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/10
 * Time: 18:49
 */
public class Demo231010 {

    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int n = in.nextInt();
            String[] strings = new String[n];

            for (int i = 0; i < n; i++) {
                strings[i] = in.next();
            }
            //String[] cases = {"none", "lengths", "lexicographically", "both"};
            char prevChar = 'a';
            int prevLength = 0;
            boolean lengthB = true;
            boolean lexB = true;
            for (int i = 0; i < n; i++) {
                int length = strings[i].length();
                char aChar = strings[i].charAt(0);
                if (length < prevLength) lengthB = false;
                if (aChar < prevChar) lexB = false;
                if (!lengthB && !lexB) {
                    System.out.println("none");
                    break;
                }
                prevLength = length;
                prevChar = aChar;
            }
            if (lengthB && lexB) System.out.println("both");
            else if (lengthB) System.out.println("lengths");
            else System.out.println("lexicographically");
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int a = in.nextInt();
            int b = in.nextInt();
            int count = a * b;

            //先求最大公因子
            if (a < b) {
                a = a ^ b;
                b = a ^ b;
                a = a ^ b;
            }
            int temp = a % b;
            while (temp != 0) {
                a = b;
                b = temp;
                temp = a % b;
            }
            System.out.println(count / b);
        }
    }

}
