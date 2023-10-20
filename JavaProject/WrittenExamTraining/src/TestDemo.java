import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/20
 * Time: 19:08
 */
public class TestDemo {
    public int[][] foundMonotoneStack(int[] nums) {
        int[][] ret = new int[nums.length][2];
        //遍历 nums
        for (int i = 0; i < nums.length; i++) {
            int[] temp = found(nums, i);
            ret[i][0] = temp[0];
            ret[i][1] = temp[1];
        }
        return ret;
    }

    private int[] found(int[] ints, int index) {
        int left = index - 1;
        int right = index + 1;
        int[] ret = new int[2];
        //先找左边
        boolean notFound = true;
        while (left >= 0) {
            if (ints[left] < ints[index]) {
                notFound = false;
                ret[0] = left;
                break;
            }
            left--;
        }
        //左边没有找到
        if (notFound) {
            ret[0] = -1;
        }
        //再找右边
        notFound = true;
        while (right < ints.length) {
            if (ints[right] < ints[index]) {
                notFound = false;
                ret[1] = right;
                break;
            }
            right++;
        }
        if (notFound) {
            ret[1] = -1;
        }
        return ret;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();//桌子的个数
            int m = in.nextInt();//客人的批次
            int[] ai = new int[n];
            for (int i = 0; i < n; i++) {
                ai[i] = in.nextInt();//每个桌子最大人数
            }
            Arrays.sort(ai);
            int[] bi = new int[m];
            int[] ci = new int[m];
            for (int i = 0; i < m; i++) {
                bi[i] = in.nextInt();//每批客人的人数
                ci[i] = in.nextInt();//每批客人的消费金额
            }
            //先算平均金额(先按人数看看要坐几张桌子,按桌子所能容纳的最多人数来当分母)
            int[] avg = new int[m];
            boolean flag = false;
            for (int i = 0; i < m; i++) {
                List<Integer> isUsed = new LinkedList<>();
                int remain = bi[i];
                while (remain > 0) {
                    int temp = findMaxTable(bi[i], ai, isUsed);
                    //要考虑所有桌子都坐不下的情况(后面再说)
                    if (temp == -1) {
                        flag = true;
                        break;
                    }
                    remain -= temp;
                }
                if (flag) {
                    continue;
                }
                //统计该批的人用了哪张桌子
                int count = 0;
                for (int temp : isUsed) {
                    count += ai[temp];
                }
                //平均金额
                avg[i] = ci[i] / count;
            }
            int firstMax = 0;
            for (int i = 0; i < m; i++) {
                if (avg[i] > firstMax) firstMax = i;
            }
            int secondMax = 0;
            for (int i = 0; i < m; i++) {
                if (avg[i] > secondMax && i != firstMax) firstMax = i;
            }
            int threeMax = 0;
            for (int i = 0; i < m; i++) {
                if (avg[i] > threeMax && i != secondMax && i != firstMax) firstMax = i;
            }

            System.out.println(ci[firstMax] + ci[secondMax] + ci[threeMax]);
        }
    }

    private static int findMaxTable(int i, int[] ai, List<Integer> list) {
        for (int j = 0; j < ai.length; j++) {
            //如果一张桌子坐得下
            if (ai[j] >= i) {
                list.add(j);
                return ai[j];
            }
        }
        //如果一张桌子坐不下,就挑目前人数能容纳最多的
        for (int j = ai.length - 1; j >= 0; j--) {
            if (!list.contains(ai[j])) return ai[j];
        }
        //如果都坐不下了
        return -1;
    }
}
