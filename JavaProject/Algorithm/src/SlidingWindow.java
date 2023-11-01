import java.util.*;

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

    //将 x 减到 0 的最小操作数: https://leetcode.cn/problems/minimum-operations-to-reduce-x-to-zero/
    public static int minOperations(int[] nums, int x) {
        //先统计一下sum O(n)
        int sum = 0;
        for (int temp : nums) {
            sum += temp;
        }

        //定义两个指针(滑动窗口)
        int left = 0; //负责左边界
        int right = 0; //负责右边界
        int target = sum - x;
        if (target < 0) {
            //1.target属于负数,也就是整个数组都不够减x
            return -1;
        }
        int count = 0; //用于统计目前窗口内的数据总和
        int maxLength = -1;
        //O(n)
        while (right < nums.length) {
            count += nums[right];
            while (count > target) {
                count -= nums[left++];
            }
            if (count == target) {
                maxLength = Math.max(maxLength, right - left + 1);
            }
            right++;
        }
        //出来后,有两种可能
        //1.不能恰好把x减0,加一个数大了,减一个数小了
        if (maxLength == -1) {
            return -1;
        }
        //2.找到了
        return nums.length - maxLength;
    }


    //水果成篮: https://leetcode.cn/problems/fruit-into-baskets/
    public int totalFruit(int[] fruits) {
        int left = 0;
        int right = 0;
        int maxLength = 0;
        HashMap<Integer, Integer> hash = new HashMap<>();//第一个Integer是种类,第二个是该种类 最新的位置(利用了哈希表的特性)
        while (right < fruits.length) {
            hash.put(fruits[right], right);//进窗口
            while (hash.size() > 2) {
                //大于两个种类了,出窗口
                int temp = hash.get(fruits[left]);
                //如果出的是这个元素的最后一个,则彻底删除该元素的记录
                if (temp == left) {
                    hash.remove(fruits[left]);
                }
                left++;
            }
            //更新结果
            maxLength = Math.max(maxLength, right - left + 1);
            right++;
        }
        return maxLength;
    }

    //找到字符串中所有字母异位词: https://leetcode.cn/problems/find-all-anagrams-in-a-string/description/
    public static List<Integer> findAnagrams(String s, String p) {
        // if (p.length() > s.length()) return null; 这种情况不考虑
        char[] charS = s.toCharArray();
        char[] charP = p.toCharArray();

        int pLength = p.length();
        int left = 0;
        int right = 0;
        //将p全部存进 map 中,用于确认是否是异位词(也可以入与 p 相同个数后,用数组记录进入的字符,然后将两者按字典顺序排序后,一一比较是否相同)
        HashMap<Character, Integer> pMap = new HashMap<>(pLength);
        for (char temp : charP) {
            if (pMap.containsKey(temp)) {
                //如果出现过了的,则需要累加次数
                int v = pMap.get(temp);
                pMap.put(temp, ++v);
            } else {
                pMap.put(temp, 1);
            }
        }
        HashMap<Character, Integer> mapWindows = new HashMap<>(pLength + 1);//第一个参数是字符,第二个参数是记录该字符在该区间内出现的次数

        //先入p个,进行比较
        for (int i = 0; i < pLength; i++) {
            char temp = charS[right];
            if (mapWindows.containsKey(temp)) {
                //如果出现过了的,则需要累加次数
                int v = mapWindows.get(temp);
                mapWindows.put(temp, ++v);
            } else {
                mapWindows.put(temp, 1);
            }
            right++;//后移
        }
        List<Integer> ret = new ArrayList<>();
        //入完后则进行判断一次
        if (checkEquals(mapWindows, pMap)) ret.add(left);

        //接着移动窗口即可
        while (right < s.length()) {
            //入窗口
            char temp = charS[right];
            if (mapWindows.containsKey(temp)) {
                //如果出现过了的,则需要累加次数
                int v = mapWindows.get(temp);
                mapWindows.put(temp, ++v);
            } else {
                mapWindows.put(temp, 1);
            }

            //判断是否需要出窗口 (因为这里的窗口大小是固定的,所以并不需要 while 进行持续出窗口)
            if (right - left + 1 > pLength) {
                char target = charS[left];
                int v = mapWindows.get(target);
                if (v > 1) {
                    //如果出现了一次以上的,则 --V 即可
                    mapWindows.put(target, --v);
                } else {
                    //只出现了一次的,直接删即可
                    mapWindows.remove(target);
                }
                left++;
            }

            //判断是否需要更新结果
            if (checkEquals(mapWindows, pMap)) ret.add(left);

            right++;
        }

        return ret;
    }

    private static boolean checkEquals(HashMap map1, HashMap map2) {
        return map1.equals(map2);
    }

    public static void main(String[] args) {
        System.out.println(findAnagrams("abab", "ab"));
    }
}
