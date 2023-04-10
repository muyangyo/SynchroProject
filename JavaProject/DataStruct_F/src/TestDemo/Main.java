package TestDemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/4
 * Time: 19:41
 */
public class Main {

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ret = new ArrayList<>();

        List<Integer> list = new ArrayList<>();
        list.add(1);

        ret.add(list);
        for (int i = 1; i < numRows; i++) {

            List<Integer> curRow = new ArrayList<>();
            curRow.add(1);
            //处理中间的数字
            for (int j = 1; j < i; j++) {
                curRow.add(ret.get(i - 1).get(j) + ret.get(i - 1).get(j - 1));
            }

            //最后一个数字1
            curRow.add(1);

            ret.add(curRow);
        }
        return ret;
    }

    public static void main(String[] args) {
        List<String> arrayList1 = new ArrayList<>();
        arrayList1.add("abc");
        arrayList1.add("def");
        arrayList1.add("abc");
        System.out.println("原本的数组: " + arrayList1);

        List<String> arrayList2 = arrayList1.subList(0, 2);
        System.out.println("截取部分: " + arrayList2);
        Iterator<String> it = arrayList1.listIterator();
        while (it.hasNext())
        System.out.println("==========================");
        System.out.println("截取部分添加 def");
        arrayList2.add("def");
        System.out.println("原本的数组: " + arrayList1);
        System.out.println("截取部分: " + arrayList2);
    }

    /*public static String func(String str1, String str2) {
        String Temp = str1;
        for (int i = 0; i < str2.length(); i++) {
            String temp = String.valueOf(str2.charAt(i));
            Temp = Temp.replaceAll(temp, "");
        }
        return Temp;
    }*/
}
