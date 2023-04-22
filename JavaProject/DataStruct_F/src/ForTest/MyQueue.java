package ForTest;

import java.util.Stack;

class MyQueue {
    Stack<Integer> stack1 = new Stack<>(); //入队用
    Stack<Integer> stack2 = new Stack<>(); //出队用

    public MyQueue() {
    }

    public void push(int x) {
        stack1.push(x);
    }

    public int pop() {
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                int x = stack1.pop();
                stack2.push(x);
            }
        }
        return stack2.pop();
    }

    public int peek() {
        if (stack2.isEmpty()) {
            while (!stack1.isEmpty()) {
                int x = stack1.pop();
                stack2.push(x);
            }
        }
        return stack2.peek();
    }

    public boolean empty() {
        if (stack1.empty() && stack2.empty()) {
            return true;
        }
        return false;
    }
}