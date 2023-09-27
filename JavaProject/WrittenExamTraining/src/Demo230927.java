import java.util.Arrays;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/27
 * Time: 22:07
 */
public class Demo230927 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = in.nextInt();
            }
            long Max = 0;
            long sum = 0;
            int slow = 0;
            int fast = 0;
            while (fast < n) {
                sum += arr[fast];
                if (sum > Max) Max = sum;
                fast++;
                //大于0就是有意义的
                if (sum < 0) {
                    slow = fast;
                    sum = 0;
                }

            }
            System.out.println(Max);
        }
    }

    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);

        while (in.hasNextLine()) {
            String str = in.nextLine();
            String insertStr = in.nextLine();

            char[] chars = str.toCharArray();
            char[] insertChars = insertStr.toCharArray();
            int ret = 0;//统计个数
            int i = 0;
            //遍历原字符串进行插入
            while (i++ < chars.length + 1) {
                char[] newChars = new char[chars.length + insertChars.length];
                int newIndex = 0;//新数组的下标

                int charsIndex = 0;//母数组的下标
                int insertCharsIndex = 0;//子数组的下标
                while (newIndex < newChars.length) {
                    //先追加母数组
                    int count = i - 1;
                    while (count > 0) {
                        newChars[newIndex] = chars[charsIndex];
                        newIndex++;
                        charsIndex++;
                        count--;
                    }
                    //追加子数组
                    while (insertCharsIndex < insertChars.length) {
                        newChars[newIndex] = insertChars[insertCharsIndex];
                        newIndex++;
                        insertCharsIndex++;
                    }
                    //再追加剩下的母数组
                    while (charsIndex < chars.length) {
                        newChars[newIndex] = chars[charsIndex];
                        newIndex++;
                        charsIndex++;
                    }
                }
                if (isHuiWen(newChars)) ret++;
            }
            System.out.println(ret);
        }
    }

    public static boolean isHuiWen(char[] chars) {
        int left = 0;
        int righjt = chars.length - 1;
        while (left < righjt) {
            if (chars[left] != chars[righjt]) return false;
            left++;
            righjt--;
        }
        return true;
    }

}
