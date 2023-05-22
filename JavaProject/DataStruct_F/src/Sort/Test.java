package Sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/5/22
 * Time:14:49
 */
public class Test {
    public static int[] MillionLevelInts() {
        int[] ints = new int[100_0000];//100w数据
        for (int i = 0; i < 100_0000; i++) {
            ints[i] = 100_0000 - i;
        }
        return ints;
    }

    public static int[] RandomInts() {
        int[] ints = new int[10_0000];//10w数据
        Random random = new Random();
        for (int i = 0; i < 10_0000; i++) {
            ints[i] = random.nextInt(1000);
        }
        return ints;
    }

    public static void TestInsertSort(int[] arr) {
        arr = Arrays.copyOf(arr, arr.length);
        long start = System.currentTimeMillis();
        Sort.insertSort(arr);
        Long end = System.currentTimeMillis();
        System.out.println("插入排序的时间:" + (end - start));
    }

    public static void main(String[] args) {
        int[] ints = MillionLevelInts();
        int[] randomInts = RandomInts();
        //测试实际时间
        TestInsertSort(ints);
        TestInsertSort(randomInts);


    }

}
