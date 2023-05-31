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

    public static void selectSort(int[] arr) {
        //每次遍历去找 当前数组范围中 的最小值
        for (int slow = 0; slow < arr.length; slow++) {
            //先假设 目前的数组范围中 slow 下标的值最小
            int minIndex = slow;
            for (int fast = slow + 1; fast < arr.length; fast++) {
                if (arr[fast] < arr[minIndex]) minIndex = fast;
            }
            swap(arr, minIndex, slow);//将 目前最小的 放到 目前数组范围 的第一个,固定其的位置
        }
    }


    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            boolean flag = true;//优化效率
            for (int j = 1; j < arr.length - i; j++) {

                if (arr[j] < arr[j - 1]) {
                    swap(arr, j - 1, j);
                    flag = false;
                }

            }
            if (flag) break;//如果 j 的一趟排序都没有换任何元素,就说明有序了
        }
    }

    public static void quickSort(int[] arr) {
        //递归版
        intervalReduction(arr, 0, arr.length - 1);
    }

    //区间缩进--递归版
    public static void intervalReduction(int[] arr, int start, int end) {
        //回返条件
        if (start >= end) return;

        //三数取中
        int mid = midOfThree(arr, start, end);

        //取中后,将基准设在 start下标处
        swap(arr, start, mid);

        //调整当前阶段(使得在 基准的左边的 比基准小,在 基准的右边的 比基准大或者相等
        int AdjustedBaselineSubscript = IntervalAdjustment(arr, start, end);

        //区间缩进
        intervalReduction(arr, start, AdjustedBaselineSubscript - 1);//调整左边
        intervalReduction(arr, AdjustedBaselineSubscript + 1, end);//调整右边
    }

    //三数取中(区间划分)---取 中间数 使得区间更加平均,使得排序整体更加高效
    private static int midOfThree(int[] arr, int start, int end) // @start--end 数组的范围   @return 返回中间值的下标
    {
        int mid = (start + end) / 2;

        //假定 start下标 为中间值  1.mid start end                  2.end start mid
        if ((arr[start] > arr[mid] && arr[start] < arr[end]) || (arr[start] < arr[mid] && arr[start] > arr[end])) {
            return start;
        }
        //假定 mid下标 为中间值 1.start mid end                     2.end mid start
        else if ((arr[mid] > arr[start] && arr[mid] < arr[end]) || (arr[mid] < arr[start] && arr[mid] > arr[end])) {
            return mid;
        }
        //end下标 为中间值 或 三个相等
        else return end;
    }

    //区间调整(挖坑法)
    private static int IntervalAdjustment(int[] arr, int start, int end) // @start--end 数组的范围   @return 返回基准所在下标
    {
        int standard = arr[start];
        while (start < end) {
            while (arr[end] >= standard && start < end) end--;//等号不能省,要把等于的也跳过
            swap(arr, end, start);//比 standard 小的,换到 start 的坑位
            while (arr[start] < standard && start < end) start++;
            swap(arr, start, end);//比 standard 大,换到 end 的坑位

            //如果start = end 了,交换本身是不影响的
        }
        //start = end 了,用 start 或 end 都一样,将基准放好
        arr[start] = standard;

        return end;
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
        int[] arr4 = {2, 5, 6, 3, 3}; // 预期输出:[2, 3, 3, 5, 6]

        //排序测试
        quickSort(arr1);
        quickSort(arr2);
        quickSort(arr3);
        quickSort(arr4);

        Test(arr1, arr2, arr3, arr4);
    }

    public static void Test(int[] arr1, int[] arr2, int[] arr3, int[] arr4) {
        System.out.println("第一个测试案例: " + ((Arrays.toString(arr1).equals("[1, 2, 3, 4, 5]")) ? "通过" : "不通过"));
        System.out.println("第二个测试案例: " + ((Arrays.toString(arr2).equals("[1, 2, 3, 4, 5]")) ? "通过" : "不通过"));
        System.out.println("第三个测试案例: " + ((Arrays.toString(arr3).equals("[1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9]")) ? "通过" : "不通过"));
        System.out.println("第四个测试案例: " + ((Arrays.toString(arr4).equals("[2, 3, 3, 5, 6]")) ? "通过" : "不通过"));
    }

}
