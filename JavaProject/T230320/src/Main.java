import java.util.Arrays;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/21
 * Time: 19:42
 */
public class Main {
    public static void FindTheFirstSingleLetter(String str) {
        int[] arr = new int[26];
        for (int i = 0; i < str.length(); i++) {
            arr[(str.charAt(i)) - 'a'] = ++arr[(str.charAt(i)) - 'a'];
        }

        for (int i = 0; i < str.length(); i++) {
            if (arr[(str.charAt(i)) - 'a'] == 1) System.out.println(str.charAt(i));
        }
        System.out.println(-1);
    }

    public static int countSegments(String s) {
        if (s.equals(""))
            return 0;
        String[] temp = s.split(" ");
        System.out.println(temp.length);
        return temp.length;
    }

    public static String toLowerCase(String s) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (((int) chars[i]) < 97){
                chars[i] = (char) (chars[i] + ('a' - 'A'));
            }
        }
        String str = new String(chars);
        return str;
    }

    public static void main(String[] args) {
        String str = "hello";
        str.toUpperCase();
    }

    public static void PalindromeString(String str) {

    }

}
