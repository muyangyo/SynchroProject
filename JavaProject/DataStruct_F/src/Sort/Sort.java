package Sort;

import java.util.Arrays;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/5/22
 * Time:14:37
 */
public class Sort {

    public static void insertSort(int[] arr) {
        //从1开始调整
        for (int index = 1; index < arr.length; index++) {
            int target = arr[index]; //正在调整的数字
            //从后往前走 (为什么不从前往后呢 ? 答:因为这样就等于冒泡排序了,而插入排序是默认前面是有序的和整理牌类似)
            for (int subIndex = index - 1; subIndex >= 0; subIndex--) {
                if (arr[subIndex] > target) swap(arr, subIndex + 1, subIndex); //大于则调整
                else break; //没有大于,则前面有序
            }

        }

    }

    //数组元素交换
    public static void swap(int[] arr, int Old, int New) {
        int temp = arr[Old];
        arr[Old] = arr[New];
        arr[New] = temp;
    }

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 4, 5}; // 预期输出: [1, 2, 3, 4, 5]
        int[] arr2 = {5, 4, 3, 2, 1}; // 预期输出: [1, 2, 3, 4, 5]
        int[] arr3 = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5}; // 预期输出: [1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9]

        //排序测试
        insertSort(arr1);
        insertSort(arr2);
        insertSort(arr3);


        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
        System.out.println(Arrays.toString(arr3));
    }

}
