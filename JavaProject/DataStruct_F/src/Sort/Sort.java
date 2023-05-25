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

    public static void shellSort(int[] arr) {
        int gap = arr.length / 2;
        while (gap > 0) { //不能 = 0,gap = 0,等于下一个数与前一个数间隔为0,这样怎么调整???
            shellSortHelper(arr, gap);//每次以 gap 为步进,来进行调整
            gap /= 2;//调整gap   7/2=3  3/2=1(最终调整)  1/2=0(跳出循环)
        }
    }

    private static void shellSortHelper(int[] arr, int gap) {
        //从 gap1 开始调整
        for (int index = gap; index < arr.length; index++) {
            int target = arr[index]; //正在调整的数字
            //从后往前走 (为什么不从前往后呢 ? 答:因为这样就等于冒泡排序了,而插入排序是默认前面是有序的和整理牌类似)
            for (int subIndex = index - gap; subIndex >= 0; subIndex -= gap) {  //对于subIndex的加减都变成了gap,其余一模一样
                if (arr[subIndex] > target) swap(arr, subIndex + gap, subIndex); //大于则调整
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
        shellSort(arr1);
        shellSort(arr2);
        shellSort(arr3);


        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
        System.out.println(Arrays.toString(arr3));
    }

}
