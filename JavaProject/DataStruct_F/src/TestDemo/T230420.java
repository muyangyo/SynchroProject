package TestDemo;

import java.util.Stack;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/20
 * Time: 20:25
 */
public class T230420 {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char temp = s.charAt(i);
            //左括号入栈
            if (temp == '(' || temp == '{' || temp == '[') {
                stack.push(temp);
            } else {
                //右号情况
                //栈为空,没有可以用于匹配的左括号
                if (stack.isEmpty()) {
                    return false;
                } else {
                    char e = stack.pop();
                    //判断左括号是否匹配右括号
                    {
                        if (temp == '}' && e == '{' || temp == ']' && e == '[' || temp == ')' && e == '(') continue;
                        else return false;
                    }
                }
            }
        }
        return stack.isEmpty();
    }

    public int evalRPN(String[] tokens) {
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
}
