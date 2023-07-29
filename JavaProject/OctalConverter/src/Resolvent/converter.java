package Resolvent;

import com.sun.corba.se.impl.oa.toa.TOA;

import java.util.*;

import static java.util.Spliterators.iterator;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/28
 * Time:20:43
 */
public class converter {
    private static int TARGET_BASE = 2;//目标进制 <= 10

    public static String To(int Ten, int target) {
        if (target > 10) throw new RuntimeException("只能输入 2/8/10 !");
        TARGET_BASE = target;
        List<Integer> list = new LinkedList<>();

        boolean isMinus = false;
        //负数先取正
        if (Ten < 0) {
            isMinus = true;
            Ten = Math.abs(Ten);
        }
        while (Ten != 0) {
            int num = Ten % TARGET_BASE;
            Ten /= TARGET_BASE;
            list.add(num);
        }
        if (list.isEmpty()) throw new RuntimeException("数值错误!");
        Collections.reverse(list);

        ListIterator<Integer> iterator = list.listIterator(0);
        StringBuilder stringBuilder = new StringBuilder();
        //负数追加一个 负号
        if (isMinus) stringBuilder.append("-");
        int count = 0;
        while (iterator.hasNext()) {
            stringBuilder.append("" + iterator.next());
            count++;
            if (count == 4) {
                stringBuilder.append(" ");
                count = 0;
            }
        }
        return stringBuilder.toString();
    }
}
