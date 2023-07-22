package ForOJ;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/22
 * Time:17:03
 */
public class T230722 {

    public int singleNumber(int[] nums) {
        Map<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hashMap.containsKey(nums[i])) {
                hashMap.put(nums[i], 2);
            } else
                hashMap.put(nums[i], 1);
        }

        for (int i = 0; i < nums.length; i++) {
            if (hashMap.get(nums[i]) == 1)
                return nums[i];
        }
        return -1;
    }

    public Node copyRandomList(Node head) {
        if (head == null) return null;
        Node cur = head;
        Node prev = null;
        Node newhead = null;//新头
        Map<Node, Node> map = new HashMap<>();
        while (cur != null) {
            Node temp = new Node(cur.val);
            map.put(cur, temp);
            if (cur != head) {
                prev.next = temp;
            } else {
                newhead = temp;
            }
            prev = temp;
            cur = cur.next;
        }

        //已经遍历完一遍,并创建好了新的列表
        cur = head;
        while (cur != null) {
            if (cur.random == null) {
                (map.get(cur)).random = null;
            } else {
                (map.get(cur)).random = map.get(cur.random);
            }
            cur = cur.next;
        }
        return newhead;
    }

    public boolean containsDuplicate(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i]))
                return true;
            else
                map.put(nums[i], 1);
        }
        return false;
    }


    // nums[i] == nums[j] 且 abs(i - j) <= k
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();//Key为数字,V为下标

        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i]) && Math.abs(map.get(nums[i]) - i) <= k)
                return true;
            else
                map.put(nums[i], i);
        }
        return false;
    }
}
