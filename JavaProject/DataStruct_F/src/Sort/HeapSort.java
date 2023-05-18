package Sort;

import java.util.PriorityQueue;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/5/18
 * Time: 19:20
 */
public class HeapSort {

    public int[] smallestK(int[] arr, int k) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();

        for (int i = 0; i < arr.length; i++) {
            priorityQueue.offer(arr[i]);//每次是 logn
        }

        int[] ints = new int[k];
        for (int i = 0; i < k; i++) {
            ints[i] = priorityQueue.poll();//每次是 logn
        }

        //所以总时间复杂度是 O( n*logn + k*logn ) --> O(n*logn)
        return ints;
    }


}
