import com.sun.javafx.scene.layout.region.SliceSequenceConverter;

import java.lang.reflect.Array;
import java.util.*;

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

    //快乐数: https://leetcode.cn/problems/happy-number/description/
    public static boolean isHappy(int n) {
        int slow = n;
        int fast = n;
        boolean first = true;
        while (slow != fast || first) {
            if (first) {
                first = false;
            }
            slow = getSum(slow);//走一步
            fast = getSum(getSum(fast));//走两步
        }
        //相遇了,则说明已经在形成的环的地方相遇.那么只有两种可能
        //1.两者都为 1
        //2.两者不为 1
        if (slow == 1) {
            return true;
        }
        return false;
    }

    public static int getSum(int x) {
        int sum = 0;
        while (x != 0) {
            int temp = x % 10;
            sum += temp * temp;
            x /= 10;
        }
        return sum;
    }


    //盛最多水的容器: https://leetcode.cn/problems/container-with-most-water/description/
    public static int maxArea(int[] height) {
        int max = 0;
        int left = 0; //左指针
        int right = height.length - 1; //右指针
        while (left < right) //不能等于,因为等于后宽度为 0,面积一定为 0
        {
            //由于宽度一直在减小,所以要找高度是比原本高的
            int area = (right - left) * Math.min(height[left], height[right]);
            if (area > max) max = area;
            if (height[left] > height[right]) {
                int temp = height[right];
                while (left < right) {
                    right--;
                    if (temp < height[right]) {
                        break;//没有比原本高,则不需要计算
                    }
                }
            } else {
                int temp = height[left];
                while (left < right) {
                    left++;
                    if (temp < height[left]) {
                        break;
                    }
                }
            }

        }
        return max;
    }

    //有效三角形的个数: https://leetcode.cn/problems/valid-triangle-number/
    public static int triangleNumber(int[] nums) {
        Arrays.sort(nums);//必须先排序
        int ret = 0;


        for (int i = nums.length - 1; i > 1; i--) {
            int left = 0;
            int right = i - 1;
            while (left < right) {
                if (nums[left] + nums[right] > nums[i]) {
                    ret += (right - left);
                    right--;//走下一个区间
                } else {
                    left++;//走下一个区间
                }
            }
        }

        return ret;
    }


    //剑指 Offer 57. 和为s的两个数字: https://leetcode.cn/problems/he-wei-sde-liang-ge-shu-zi-lcof/
    public static int[] twoSum(int[] nums, int target) {
        //该题使用双指针双减法的前提: nums 为递增数组
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            //int sum = nums[left] + nums[right];
            if (nums[left] + nums[right] > target) {
                right--;
            } else if (nums[left] + nums[right] == target) {
                return new int[]{nums[left], nums[right]};
            } else {
                left++;
            }
        }
        return null;
    }

    //三数之和: https://leetcode.cn/problems/3sum/
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);//排序

        //寻找数字
        Set<List<Integer>> set = new HashSet<>();//用于去重
        for (int j = 0; j <= nums.length - 3; j++) {
            //内部和 两数和 一样
            int left = j + 1;
            int right = nums.length - 1;

            while (left < right) {
                int temp = nums[left] + nums[right];
                if (temp + nums[j] > 0) {
                    right--;
                } else if (temp + nums[j] == 0) {
                    List<Integer> list = new ArrayList<>(Arrays.asList(nums[j], nums[left], nums[right]));
                    set.add(list);
                    left++;
                    right--;
                } else {
                    left++;
                }
            }

        }

        List<List<Integer>> ret = new ArrayList<>(set);
        return ret;
    }

    //四数之和: https://leetcode.cn/problems/4sum/
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        //与三数和一样,只是多了一层循环而已
        Arrays.sort(nums);//排序

        Set<List<Integer>> set = new HashSet<>();
        for (int i = 0; i <= nums.length - 4; i++) {
            for (int j = i + 1; j <= nums.length - 3; j++) {
                int left = j + 1;
                int right = nums.length - 1;

                while (left < right) {
                    long count = (long) nums[j] + nums[left] + nums[right] + nums[i];
                    if (count > target) {
                        right--;
                    } else if (count == target) {
                        List<Integer> list = new ArrayList<>(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        set.add(list);
                        right--;
                        left++;
                    } else {
                        left++;
                    }
                }
            }
        }
        List<List<Integer>> ret = new ArrayList<>(set);
        return ret;
    }

    public static void main(String[] args) {
    }
}
