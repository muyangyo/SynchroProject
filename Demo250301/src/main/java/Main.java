import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/3/1
 * Time: 10:42
 */


public class Main {
    static class ListNode {
        int val;
        ListNode next = null; // 链表格式

        public ListNode(int val) {
            this.val = val;
        }
    }

    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     * @param lists ListNode类ArrayList
     * @return ListNode类
     */
    public static ListNode mergeKLists(ArrayList<ListNode> lists) {
        PriorityQueue<ListNode> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a.val));
        for (ListNode node : lists) {
            while (node != null) {
                priorityQueue.offer(node);
                node = node.next;
            }
        }
        //生成了pq
        priorityQueue.stream().forEach((node) -> {
            System.out.println(node.val);
        });

        ListNode ret = priorityQueue.remove();
        ListNode temp = ret;
        for (ListNode node : priorityQueue) {
            temp.next = node;
            temp = node.next;
        }
            temp.next = null;
        return ret;
    }

    // 辅助函数：打印链表
    public static void printList(ListNode head) {
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val + " ");
            current = current.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {

        // 测试用例 1
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {4, 5, 6, 7};
        ListNode list1 = arrayToList(arr1);
        ListNode list2 = arrayToList(arr2);
        ListNode[] lists1 = {list1, list2};
        ListNode result1 = mergeKLists(new ArrayList<>(Arrays.asList(lists1)));
        printList(result1);
    }

    // 辅助函数：将数组转换为链表
    public static ListNode arrayToList(int[] arr) {
        if (arr == null || arr.length == 0) return null;
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (int num : arr) {
            current.next = new ListNode(num);
            current = current.next;
        }
        return dummy.next;
    }
}
