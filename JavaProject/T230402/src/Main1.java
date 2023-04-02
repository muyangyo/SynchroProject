import java.util.Arrays;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/2
 * Time: 19:14
 */
public class Main1 {
    public static void rotate(int[] nums, int k) {
        while (k % (nums.length) != 0) {
            k--;
            int temp = nums[nums.length - 1];
            Swap(nums);
            nums[0] = temp;
        }
    }

    public static void Swap(int[] nums) {
        for (int i = nums.length - 1; i > 0; i--) {
            nums[i] = nums[i - 1];
        }
    }

    public static int missingNumber(int[] nums) {
        int count = ((nums.length) * (nums.length + 1)) / 2;
        for (int i = 0; i < nums.length; i++) {
            count -= nums[i];
        }
        return count;
    }

    //三翻转法
    public static void ThreeOverMethod(int[] arr, int k) {
        k = k % arr.length;//超出 arr.length 就是重新翻转一遍(k==arr.length就是不翻转)
        //先对全部进行翻转
        Reverse(arr, 0, arr.length - 1);

        //以 第 k 位(下标为k-1) 与 第 k+1(下标为k) 位 之间为分界线 ,左右各自翻转
        Reverse(arr, 0, k - 1);
        Reverse(arr, k, arr.length - 1);
    }

    public static void Reverse(int[] arr, int start, int end) {
        while (start <= end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }


    public int RemoveAllElement(int[] nums, int val) {
        if (nums == null) {
            return 0;
        }
        int fast = 0;
        int slow = 0;
        while (fast < nums.length) {
            if (nums[fast] != val) {
                nums[slow] = nums[fast];
                slow++;
                fast++;
            } else {
                fast++;
            }
        }
        return slow;
    }

    public static int removeDuplicates(int[] nums) {
        if (nums == null) {
            return 0;
        } else if (nums.length == 1) {
            return 1;
        }

        int fast = 1;
        int slow = 0;
        while (fast < nums.length) {
            if (nums[fast] == nums[slow]) {
                while (fast < nums.length && nums[fast] == nums[slow]) {
                    fast++;
                }
                if (fast == nums.length) {
                    return slow + 1;
                }
                slow++;
                nums[slow] = nums[fast];
            } else {
                slow++;
                fast++;
            }

        }
        return slow + 1;
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        if (m == 0) {
            nums1[0] = nums2[0];
        }
        int pos1 = m - 1;
        int pos2 = n - 1;
        int pos3 = nums1.length - 1;
        while (pos1 >= 0 && pos2 >= 0) {
            if (nums1[pos1] > nums2[pos2]) {
                nums1[pos3] = nums1[pos1];
                pos1--;
            } else {
                nums1[pos3] = nums2[pos2];
                pos2--;
            }
            pos3--;
        }
        if (pos1 < 0) {
            System.arraycopy(nums2, 0, nums1, 0, pos2 + 1);
        } else if (pos2 < 0) {
            System.arraycopy(nums1, 0, nums1, 0, pos1 + 1);
        }
    }

    public static void main(String[] args) {
        int[] ints = {2, 0};
        int[] ints1 = {1};
        merge(ints, 1, ints1, 1);
        System.out.println(Arrays.toString(ints));
    }
}
