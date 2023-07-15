package Sort;

import java.util.*;

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
        //intervalReduction(arr, 0, arr.length - 1);
        //非递归版
        intervalReductionNonRec(arr, 0, arr.length - 1);
    }

    //区间缩小--递归版
    public static void intervalReduction(int[] arr, int start, int end) {
        //回返条件
        if (start >= end) return;

        //三数取中
        int mid = midOfThree(arr, start, end);

        //取中后,将基准设在 start下标处
        swap(arr, start, mid);

        //调整当前阶段(使得在 基准的左边的 比基准小,在 基准的右边的 比基准大或者相等
        int AdjustedBaselineSubscript = IntervalAdjustment(arr, start, end);

        //区间缩小
        intervalReduction(arr, start, AdjustedBaselineSubscript - 1);//调整左边
        intervalReduction(arr, AdjustedBaselineSubscript + 1, end);//调整右边
    }

    //区间缩小--非递归版
    public static void intervalReductionNonRec(int[] arr, int start, int end) {
        if (arr == null) return;//先排除为 null 的情况
        Stack<Integer> stack = new Stack<>();
        //先压入要 处理的完整范围
        stack.push(start);
        stack.push(end);

        while (!stack.isEmpty()) {
            //取要调整的区间
            end = stack.pop();//由于我们先压入的是 头 ,后压入 尾 ,那么取数据就要先取 尾 ,再取 头 .
            start = stack.pop();

            //三数取中
            int mid = midOfThree(arr, start, end);

            //取中后,将基准设在 start下标处
            swap(arr, start, mid);

            //调整当前阶段(使得在 基准的左边的 比基准小,在 基准的右边的 比基准大或者相等
            int AdjustedBaselineSubscript = IntervalAdjustment(arr, start, end);

            //区间缩小
            //先压入右边的 (因为我们要模仿递归的思路,将 左边 处理完再回来处理 右边)
            //一个区间至少要有 两个元素 (一个元素调整自己没有意义)
            if (end - (AdjustedBaselineSubscript + 1) > 0) {
                stack.push(AdjustedBaselineSubscript + 1);
                stack.push(end);
            }

            //再压入左边的,使得其下次优先处理 左边的
            //一个区间至少要有 两个元素 (一个元素调整自己没有意义)
            if ((AdjustedBaselineSubscript - 1) - start > 0) {
                stack.push(start);
                stack.push(AdjustedBaselineSubscript - 1);
            }

        }

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
        //end下标 为中间值 或 三个相等 或 其他情况
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



    public static void heapSort(int[] arr) {
        createBigHeap(arr);//先建立好大根堆
        //八个元素,调整 7 次 就够了,所以是 7 6 5 4 3 2 1
        for (int times = arr.length - 1; times > 0; times--) {
            swap(arr, 0, times);//将最大的元素放到最后面(和堆的删除类似)
            shiftDown(arr, 0, times);//再次恢复大根堆状态,由于我们只动了 根 这棵树,所以只要调整这棵树即可
        }
    }

    public static void createBigHeap(int[] arr) {
        for (int parent = ((arr.length - 1) - 1) / 2; parent >= 0; parent--) {
            shiftDown(arr, parent, arr.length);
        }
    }


    private static void shiftDown(int[] arr, int parent, int len) { //注意这里 len 取代了 usedSize
        //这里为了实现功能提高效率,所以使用 范围(len) 进行缩范,因为我们每次都对UsedSize的长度进行调整,因为我们没有写poll操作
        int child = parent * 2 + 1;
        while (child < len) {
            if ((child + 1 < len) && (arr[child + 1] > arr[child])) {
                child = child + 1;
            }
            if (arr[child] > arr[parent])
                swap(arr, child, parent);
            else break;
            parent = child;
            child = parent * 2 + 1;
        }
    }



    public static void mergeSort(int[] arr) {
        mergeSortHelper(arr, 0, arr.length - 1);
    }

    /*
     * 使用归并排序算法对数组arr的从left到right（左闭右闭区间）进行排序
     * @param left 子数组的左边界
     * @param right 子数组的右边界
     */
    private static void mergeSortHelper(int[] arr, int left, int right) {
        //回返条件
        if (left >= right) return;//当左边界遇见或者超过右边界时,回返.(子数组中只有一个或零个元素)

        //拆分
        int mid = (left + right) / 2;

        mergeSortHelper(arr, left, mid); //不能使用mid-1,后面会报错,会遇见"下标-1"的问题
        mergeSortHelper(arr, mid + 1, right);
        merge(arr, left, mid, right);//合并
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        //左数组范围
        int leftStart = left;
        int leftEnd = mid;
        //右数组范围
        int rightStart = mid + 1;
        int rightEnd = right;


        int[] Temp = new int[right - left + 1];//暂时数组
        int index = 0;//暂时数组的下标
        //双数组合并

        while ((leftStart <= leftEnd) && (rightStart <= rightEnd)) {

            if (arr[leftStart] < arr[rightStart]) {
                Temp[index] = arr[leftStart];
                leftStart++;
            } else {
                Temp[index] = arr[rightStart];
                rightStart++;
            }

            index++;
        }
        //处理剩下的数据
        //如果剩下左数组
        while (leftStart <= leftEnd) {
            Temp[index] = arr[leftStart];
            index++;
            leftStart++;
        }
        //如果剩下有数组
        while (rightStart <= rightEnd) {
            Temp[index] = arr[rightStart];
            index++;
            rightStart++;
        }


        //将排序好的元素从临时数组复制回原始数组
        for (int i = 0; i < Temp.length; i++) {
            arr[i + left] = Temp[i];//arr不能直接用i,因为在右边的时候,是从 "left" 下标开始的,并不是 "0" 下标开始的
        }
    }

    //计数排序
    public static void countSort(int[] arr) {

        //先遍历一遍找范围区间
        int minVal = arr[0];//不能使用 0 初始化,会导致最小值出问题
        int maxVal = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < minVal) minVal = arr[i];
            if (arr[i] > maxVal) maxVal = arr[i];
        }

        int[] ints = new int[maxVal - minVal + 1];//创建一个数组,统计元素

        //统计元素,遍历原数组
        for (int i = 0; i < arr.length; i++) {
            int index = arr[i] - minVal; //以 val - minVal 为下标统计元素
            ints[index]++;
        }

        //重新排序原数组
        int index = 0;
        for (int i = 0; i < ints.length; i++) {
            while (ints[i] > 0) {
                arr[index] = i + minVal;
                index++;//原数组向后
                ints[i]--;//弹出元素
            }
        }
    }



    //基数排序 todo:无法解决负数
    public static void RadixSort(int[] arr) {
        //获取数组中最大数字的位数，并将其作为排序的次数
        int times = getMaxDigit(arr);

        Queue<Integer>[] queues = new Queue[10];//十进制都是 0 - 9 构成的,所以只需要10个队列即可
        //实例化对象,由于类数组不像普通的整形数组,它未初始化时为 null,不是为 0
        for (int i = 0; i < queues.length; i++) {
            queues[i] = new LinkedList<Integer>();
        }

        for (int Time = 1; Time <= times; Time++) {

            //遍历,入队
            for (int i = 0; i < arr.length; i++) {
                int index = getNum(arr[i], Time);
                queues[index].offer(arr[i]);
            }

            //出队,重排
            int index = 0;
            int i = 0;
            while (index < queues.length) {
                //不能用 if ,因为可能有多个元素
                while (!queues[index].isEmpty()) {
                    arr[i] = queues[index].poll();
                    i++;
                }
                index++;
            }

        }

    }

    /**
     * 从整数 x 中获取从右往左数第 digit 位数字
     *
     * @param x     要获取数字的整数
     * @param digit 要获取的数字位数，从右往左数，最右边是第 1 位
     * @return 整数 x 中从右往左数第 digit 位的数字
     */
    private static int getNum(int x, int digit) {
        int digitNum = 0;
        while (digit > 0) {
            digitNum = x % 10;
            x /= 10;
            digit--;
        }
        return digitNum;
    }

    private static int getMaxDigit(int[] arr) {
        arr = Arrays.copyOf(arr, arr.length);
        int maxDigit = 0;//最长的数字
        for (int i = 0; i < arr.length; i++) {
            int count = 0;
            //算位数
            while (arr[i] != 0) {
                arr[i] /= 10;
                count++;
            }
            if (count > maxDigit) maxDigit = count;
        }

        return maxDigit;
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
        int[] arr5 = {123, 222, 111, 298, 988, 277}; // 预期输出:[111, 123, 222, 277, 298, 988]

        //排序测试
        mergeSort(arr1);
        mergeSort(arr2);
        mergeSort(arr3);
        mergeSort(arr4);
        mergeSort(arr5);

        Test(arr1, arr2, arr3, arr4, arr5);
    }

    public static void Test(int[] arr1, int[] arr2, int[] arr3, int[] arr4, int[] arr5) {
        System.out.println("第一个测试案例: " + ((Arrays.toString(arr1).equals("[1, 2, 3, 4, 5]")) ? "通过" : "不通过"));
        System.out.println("第二个测试案例: " + ((Arrays.toString(arr2).equals("[1, 2, 3, 4, 5]")) ? "通过" : "不通过"));
        System.out.println("第三个测试案例: " + ((Arrays.toString(arr3).equals("[1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 9]")) ? "通过" : "不通过"));
        System.out.println("第四个测试案例: " + ((Arrays.toString(arr4).equals("[2, 3, 3, 5, 6]")) ? "通过" : "不通过"));
        System.out.println("第五个测试案例: " + ((Arrays.toString(arr5).equals("[111, 123, 222, 277, 298, 988]")) ? "通过" : "不通过"));

    }

}
