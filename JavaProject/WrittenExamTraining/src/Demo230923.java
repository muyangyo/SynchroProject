import java.sql.Array;
import java.util.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/23
 * Time: 21:12
 */
public class Demo230923 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            long[] arr = new long[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextLong();
            }

            int fast = 1;
            int slow = 0;
            int count = 1;

            boolean increase = false;
            boolean decrease = false;
            while (fast < n) {
                if (arr[fast] > arr[slow]) increase = true;
                else if (arr[fast] == arr[slow]) ;
                else decrease = true;

                if (increase && decrease) {
                    count++;
                    decrease = false;
                    increase = false;
                }
                fast++;
                slow++;
            }
            System.out.println(count);

        }
    }

    /*//简易实现
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            String[] strs = str.split(" |\n");
            for (int i = strs.length - 1; i >= 0; i--) {
                System.out.print(strs[i] + " ");
            }
        }
    }*/


    public static void main2(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            int fast = 0;
            int slow = 0;
            char[] chars = str.toCharArray();
            List<String> list = new LinkedList<>();
            StringBuilder stringBuilder;
            while (fast < chars.length) {
                if (chars[fast] == ' ') {
                    stringBuilder = new StringBuilder();
                    while (slow <= fast) {
                        stringBuilder.append(chars[slow]);
                        slow++;
                    }
                    list.add(stringBuilder.toString());
                }
                fast++;
            }
            stringBuilder = new StringBuilder();
            while (slow < fast) {
                stringBuilder.append(chars[slow]);
                slow++;
            }
            list.add(stringBuilder.toString());
            Collections.reverse(list);
            boolean first = true;
            for (String temp : list) {
                if (first) {
                    System.out.print(temp);
                    first = false;
                } else
                    System.out.print(temp + " ");
            }

        }
    }


}

