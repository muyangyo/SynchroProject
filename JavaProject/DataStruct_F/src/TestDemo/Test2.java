package TestDemo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

        ListIterator<Integer> integerListIterator = list.listIterator(0);
        while(integerListIterator.hasNext()){
            System.out.print(integerListIterator.next() + " | ");
        }
        System.out.println();

        /*List<Integer> list1 = new ArrayList<>();*/
    }
}
