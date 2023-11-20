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
    //todo: 待确定是不是可以使用 break 解决问题
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

    public static void main(String[] args) {
        System.out.println(searchInsert(new int[]{1, 3, 5, 6}, 2));
    }
}
