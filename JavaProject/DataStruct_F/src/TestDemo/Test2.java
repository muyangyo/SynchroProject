package TestDemo;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/11
 * Time: 19:11
 */
public class Test2 {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);

        //使用迭代器打印数据
        ListIterator<Integer> integerListIterator = list.listIterator(0);
        while (integerListIterator.hasNext()) {
            System.out.print(integerListIterator.next() + " | ");
        }
        System.out.println();

        //直接调用底层的toString方法
        System.out.println(list);
        list.clear();
    }
}

