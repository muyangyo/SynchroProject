import java.util.HashMap;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/11
 * Time: 9:51
 */
public class SlidingWindow {

    //长度最小的子数组: https://leetcode.cn/problems/minimum-size-subarray-sum/
    public int minSubArrayLen(int target, int[] nums) {
        int sum = 0;//统计当前窗口内的数据总和
        int length = nums.length;//统计当前窗口的长度(为了好判断,先使用 nums.length 初始化)
        int slow = 0;//进窗口 (左闭)
        int fast = 0;//出窗口 (右开)

        //第一次先进行一个可能为最长的,但是一定符合条件的长度
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (sum >= target) {
                length = i + 1;
                fast = i + 1;//左闭右开
                break;
            }
        }
        //出来有两种情况
        //1.找到了一个符合条件的长度
        if (sum >= target) {

            while (fast <= nums.length) {

                while (sum >= target) {
                    //出窗口,减小窗口大小
                    sum = sum - nums[slow];
                    slow++;

                    //判断是否需要更新
                    if (sum >= target) {
                        int temp = fast - slow;
                        if (temp < length) length = temp;//更新长度
                    }
                }
                //此时肯定是小于target了,要增加窗口大小,使后一个元素进窗口
                fast++;
                if (fast > nums.length) break; //避免 fast 原本就等于 nums.length,再 ++ 就变成 nums.length+1
                //如果此时 -1 ,依旧会越界访问,那提前跳出循环即可(肯定是不需要再进循环的了)
                sum += nums[(fast - 1)]; //注意不是 fast-- !这是为了左闭右开
            }

        }
        //2.遍历完了数组,依旧没有达到 target
        else {
            return 0;
        }
        return length;
    }

    //无重复字符的最长子串: https://leetcode.cn/problems/longest-substring-without-repeating-characters/
    public static int lengthOfLongestSubstring(String s) {
        HashMap<Character, Integer> hashMap = new HashMap<>();//用于记录 当前窗口内 是否有重复字符
        int slow = 0;
        int fast = 0;
        int length = 0;
        char[] arr = s.toCharArray();

        while (fast < arr.length) {
            //1.进窗口,扩大窗口大小
            hashMap.put(arr[fast], hashMap.containsKey(arr[fast]) ? 2 : 1);//2 表示已经出现过了,1 表示第一次出现

            //2.判断是否有重复的字符
            //if (hashMap.containsKey(arr[fast]))不能用这个
            while (hashMap.getOrDefault(arr[fast], 0) > 1) {
                //3.出窗口
                hashMap.remove(arr[slow]);
                if (!hashMap.containsKey(arr[fast]))
                    hashMap.put(arr[fast], 1);//把后面一个多删的元素添加回去
                slow++;
            }

            //X.更新结果
            length = Math.max(fast - slow + 1, length);

            fast++;//续 1.
        }
        return length;
    }

    //最大连续1的个数 III: https://leetcode.cn/problems/max-consecutive-ones-iii/description/
    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int zeroCount = 0;
        int maxLength = 0;
        while (right < nums.length) {
            if (nums[right] == 0) {
                zeroCount++;
                //当 0 的个数超过能容纳的个数时，需要抛出先入窗口的零,直到到可容纳范围
                if (zeroCount > k) {
                    while (zeroCount > k) {
                        if (nums[left] == 0) zeroCount--;
                        left++;
                    }
                }
            }
            int temp = right - left + 1;
            if (temp > maxLength) maxLength = temp;
            right++;
        }
        return maxLength;
    }


}
