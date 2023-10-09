import java.util.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/9
 * Time: 10:00
 */
public class Demo231009 {
    public static boolean chkParenthesis(String A, int n) {
        if (n % 2 == 1) return false;
        Map<Character, Character> map = new HashMap<>();
        map.put('(', ')');
        map.put('[', ']');
        map.put('{', '}');

        char[] chars = A.toCharArray();
        Stack<Character> stack = new Stack<>();
        int index = 0;
        while (index < chars.length) {
            char temp = chars[index];
            if (temp == '(' || temp == '[' || temp == '{') {
                stack.push(temp);
            } else if (temp == ')' || temp == '}' || temp == ']') {
                if (stack.size() == 0) return false;
                char check = map.get(stack.peek());
                if (check != temp) return false;
                stack.pop();
            } else {
                return false;
            }
            index++;
        }
        //遍历完成数组
        return stack.size() == 0;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[] fibs = new int[40];
        fibs[0] = 0;
        fibs[1] = 1;
        int index = 2;
        int a = 0;
        int b = 1;
        int c = a + b;
        boolean first = true;
        while (index < fibs.length) {
            if (first) {
                first = false;
            } else {
                c = a + b;
            }
            a = b;
            b = c;
            fibs[index++] = c;
        }//创建斐波那契数列的数据
        //System.out.println(Arrays.toString(fibs));
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int ret = Integer.MAX_VALUE;
            int i = 0;
            while (i < fibs.length) {
                int temp = fibs[i];
                if (temp > n) {
                    ret = Math.min(Math.abs(fibs[i - 1] - n), fibs[i] - n);
                    break;
                }
                i++;
            }
            System.out.println(ret);
        }

    }
}
