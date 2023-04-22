package TestDemo;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/22
 * Time: 21:00
 */
public class T230422 {
    public static void main(String[] args) {
        Deque<Integer> queue = new LinkedList<>();
        queue.offer(1);
        System.out.println(queue);

        Deque<Integer> stack = new LinkedList<>();
        stack.push(1);
        System.out.println(stack);
    }
}
