import com.sun.xml.internal.ws.encoding.policy.MtomPolicyMapConfigurator;

import java.util.Arrays;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/20
 * Time: 10:51
 */
public class BinarySearch {
    //在排序数组中查找元素的第一个和最后一个位置: https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/description/
    public int[] searchRange(int[] nums, int target) {
        int[] ret = {-1, -1};
        if (nums.length == 0) {
            return ret; //避免空指针异常
        }
        //先找左端点
        int left = 0;
        int right = nums.length - 1;
        int mid = left + (right - left) / 2; //因为 right 更新表达式为 right = mid ,为了避免死循环,所以采用 左中点 的表达式
        while (left < right) {
            if (nums[mid] < target) {
                //在左区间 x < target
                //更新左边界
                left = mid + 1;
            } else {
                //在右区间 x >= target
                //更新右边界
                right = mid;
            }
            mid = left + (right - left) / 2;
        }

        if (left == right && nums[left] == target) {
            ret[0] = left; //此时 left == right 恰好处于端点,如果 nums[left] == target 不相等则必定没有
        }
        //再找右端点
        left = 0;
        right = nums.length - 1;
        mid = left + (right - left + 1) / 2; //因为 left 更新表达式为 left = mid ,为了避免死循环,所以采用 右中点 的表达式
        while (left < right) {
            if (nums[mid] <= target) {
                //在左区间 x <= target
                //更新左边界
                left = mid;
            } else {
                //在右区间 x > target
                //更新右边界
                right = mid - 1;
            }
            mid = left + (right - left + 1) / 2;
        }
        if (left == right && nums[left] == target) {
            ret[1] = left;//此时 left == right 恰好处于端点
        }

        return ret;
    }

    //x 的平方根 : https://leetcode.cn/problems/sqrtx/
    public int mySqrt(int x) {
        long left = 0;
        long right = x;
        long mid = left + (right - left + 1) / 2;
        while (left <= right) {
            if (left == right) {
                return (int) left;
            } else if (mid * mid <= x) {
                left = mid;
            } else {
                right = mid - 1;
            }
            mid = left + (right - left + 1) / 2;
        }
        return 0;
    }


    //搜索插入位置: https://leetcode.cn/problems/search-insert-position/description/
    public static int searchInsert(int[] nums, int target) {
        //这题就是取端点,左端点或者右端点都可以
        int left = 0;
        int right = nums.length - 1;
        int mid = left + (right - left + 1) / 2;
        while (left <= right) {
            if (left == right) {
                //此时为端点
                break;
            }//这里取右端点
            else if (nums[mid] <= target) {
                left = mid;
            } else {
                right = mid - 1;
            }
            mid = left + (right - left + 1) / 2;
        }
        //出来了,找到端点了,那么就有这些情况
        //1.端点值本身就是 target 或者 小于 数组头
        if (nums[left] == target || nums[0] > target) {
            return left;
        }
        //2.为数组最末尾之后 或者 虽然不存在这个数,但是是属于区间内的
        return left + 1;
    }

    //山脉数组的峰顶索引: https://leetcode.cn/problems/peak-index-in-a-mountain-array/description/
    public static int peakIndexInMountainArray(int[] arr) {
        int left = 1;
        int right = arr.length - 2;

        //直接找左端点即可
        while (left <= right) {
            int mid = left + (right - left) / 2;//左中点
            if (left == right) {
                break;
            }
            //如果在右区间
            else if (arr[mid] > arr[mid + 1]) {  //不能使用 arr[mid - 1] > arr[mid] 来进行判断
                right = mid;
            }
            //如果在左区间
            else {
                left = mid + 1;
            }
        }
        return left;
    }

    //寻找旋转排序数组中的最小值: https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/description/
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int stand = nums[nums.length - 1];
        //找左端点
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (left == right) {
                break;
            } else if (nums[mid] > stand) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return nums[left];
    }

    //寻找峰值: https://leetcode.cn/problems/find-peak-element/description/
    public int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        //找右端点
        while (left <= right) {
            int mid = left + (right - left + 1) / 2;
            if (left == right) {
                break;
            } else if (nums[mid] > nums[mid - 1]) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
}
