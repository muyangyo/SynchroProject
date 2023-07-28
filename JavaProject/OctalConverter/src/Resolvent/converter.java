package Resolvent;

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
    private final static int TARGET_BASE = 2;

    public static String To(int Ten) {
        List<Integer> list = new LinkedList<>();
        while (Ten != 0) {
            int num = Ten % TARGET_BASE;
            Ten /= TARGET_BASE;
            list.add(num);
        }
        if (list.isEmpty())
            throw new RuntimeException("数值错误!");
        Collections.reverse(list);

        ListIterator<Integer> iterator = list.listIterator(0);
        StringBuilder stringBuilder = new StringBuilder();
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
