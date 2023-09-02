import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/2
 * Time: 16:01
 */
public class DoublePointer {
    //移动零: https://leetcode.cn/problems/move-zeroes/
    public static void moveZero(int[] nums) {
        if (nums.length == 0 || nums.length == 1) {
            return;
        }
        int cur = 0;
        int slowIndex = 0;

        for (cur = 0; cur < nums.length; cur++) {
            if (nums[cur] != 0) {
                swap(nums, cur, slowIndex);
                slowIndex++;
            }
        }

    }

    public static void swap(int[] arr, int oldPos, int newPos) {
        int temp = arr[oldPos];
        arr[oldPos] = arr[newPos];
        arr[newPos] = temp;
    }

    //复写零: https://leetcode.cn/problems/duplicate-zeros/description/
    public static void duplicateZeros(int[] arr) {
        int firstCur = 0;//预先演算的原数组下标
        int firstDest = -1;//预先演算的结果下标(必须是 -1 起步,)
        //先进行预先演算
        for (firstCur = 0; firstCur < arr.length; firstCur++) {
            if (arr[firstCur] != 0) {
                //非零的数,复写一次
                firstDest++;
            } else {
                //为零的数,复写两次
                firstDest += 2;
            }

            if (firstDest >= arr.length - 1) {
                break;//此时 firstCur 的数即为结果的最后一个数
            }
        }

        int cur = firstCur;//此时为最后一个数的下标
        int dest = arr.length - 1;
        if (firstDest >= arr.length) {
            //特殊情况
            arr[dest] = arr[cur];
            dest--;
            cur--;
        }
        for (; dest >= 0; dest--) {
            if (arr[cur] == 0) {
                arr[dest] = arr[cur];
                dest--;
                arr[dest] = arr[cur];
            } else {
                arr[dest] = arr[cur];
            }
            cur--;

        }

    }

    public static void main(String[] args) {
        int[] arr = {1, 5, 2, 0, 6, 8, 0, 6, 0};
        duplicateZeros(arr);
    }
}
