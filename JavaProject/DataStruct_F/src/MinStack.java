import java.util.Stack;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/13
 * Time: 19:21
 */

//类说明:
// https://leetcode.cn/problems/min-stack/
public class MinStack {
    private Stack<Integer> stack = new Stack<>();  //有感栈
    private Stack<Integer> minStack = new Stack<>();  //无感栈

    public MinStack(int val) {
        stack.push(val);
        minStack.push(val);
    }

    public void push(int val) {
        if (stack.isEmpty()) {
            stack.push(val);
            minStack.push(val);
        } else if (val <= minStack.peek()) {
            stack.push(val);
            minStack.push(val);
        } else {
            stack.push(val);
        }

    }

    public void pop() {
        int x = stack.pop();
        if (x == minStack.peek()) {
            minStack.pop();
        }
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }
}
