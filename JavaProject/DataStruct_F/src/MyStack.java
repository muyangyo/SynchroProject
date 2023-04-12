import java.util.Arrays;
import java.util.Stack;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/11
 * Time: 19:37
 */
public class MyStack {
    private int[] ints = {0, 0, 0, 0, 0};
    private int UsedSize;

    public void push(int data) {
        isFull();
        ints[UsedSize] = data;
        UsedSize++;
    }

    private void isFull() {
        if (UsedSize == ints.length) {
            ints = Arrays.copyOf(ints, ints.length << 2);
        }
    }

    public int pop() {
        int e = peek();
        UsedSize--;
        return e;
    }

    public int peek() {
        if (empty()) {
            throw new RuntimeException("栈为空，" +
                    "无法获取栈顶元素");
        }
        return ints[UsedSize - 1];
    }

    public int size() {
        return UsedSize;
    }

    public boolean empty() {
        return UsedSize == 0;
    }

    //stack.push((int) temp - '0')
    //给你一个字符串数组 tokens ，表示一个根据 逆波兰表示法 表示的算术表达式。
    //请你计算该表达式。返回一个表示表达式值的整数。
    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        int count = 0;
        for (int i = 0; i < tokens.length; i++) {
            if (isNum(tokens[i])) {
                int a = Integer.valueOf(tokens[i]);
                stack.push(a);
            } else {
                int y = stack.pop();
                int z = stack.pop();
                switch (tokens[i]) {
                    case "+":
                        count = z + y;
                        stack.push(count);
                        break;
                    case "-":
                        count = z - y;
                        stack.push(count);
                        break;
                    case "*":
                        count = z * y;
                        stack.push(count);
                        break;
                    case "/":

                        count = z / y;
                        stack.push(count);
                        break;
                }
            }
        }
        if (stack.isEmpty()) {
            return count;
        } else
            return stack.pop();
    }

    private static boolean isNum(String str) {
        if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/"))
            return false;
        return true;
    }


    public static void main(String[] args) {
        MyStack stack = new MyStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        int count = 13 / 5;
        System.out.println(count);
        String[] strs = {"4", "13", "5", "/", "+"};
        System.out.println(evalRPN(strs));
    }
}
