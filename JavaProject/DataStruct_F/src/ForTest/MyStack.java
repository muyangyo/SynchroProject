package ForTest;

import java.util.LinkedList;
import java.util.Queue;

class MyStack {
    Queue<Integer> queue1 = new LinkedList<>();
    Queue<Integer> queue2 = new LinkedList<>();

    public MyStack() {
    }

    public void push(int x) {
        if (queue1.isEmpty()) {
            queue2.offer(x);
        } else {
            queue1.offer(x);
        }
    }

    public int pop() {
        if (queue1.isEmpty()) {
            while (queue2.size() > 1) {
                int x = queue2.poll();
                queue1.offer(x);
            }
            return queue2.poll();
        } else {
            while (queue1.size() > 1) {
                int x = queue1.poll();
                queue2.offer(x);
            }
            return queue1.poll();
        }
    }

    public int top() {
        if (queue1.isEmpty()) {
            int x = 0;
            while (queue2.size() > 0) {
                x = queue2.poll();
                queue1.offer(x);
            }
            return x;
        } else {
            int x = 0;
            while (queue1.size() > 0) {
                x = queue1.poll();
                queue2.offer(x);
            }
            return x;
        }
    }

    public boolean empty() {
        if (queue1.isEmpty() && queue2.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        MyStack myStack = new MyStack();
        myStack.push(1);
        myStack.push(2);
        System.out.println(myStack.top()); // 返回 2
        System.out.println(myStack.pop()); // 返回 2
        System.out.println(myStack.empty()); // 返回 False
    }
}