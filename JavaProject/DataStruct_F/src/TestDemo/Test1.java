package TestDemo;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/8
 * Time: 14:41
 */

class Example {
    public static <T extends Comparable> void FIndMax(T[] array) {
        T max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(max) > 0) {
                max = array[i];
            }
        }
        System.out.println(max);
    }
}

public class Test1 {
    public static void main(String[] args) {
        Integer[] integers = {1, 3, 0, 18, 8};
        Example.FIndMax(integers);
    }
}
