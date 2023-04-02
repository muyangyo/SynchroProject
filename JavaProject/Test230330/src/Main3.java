/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/2
 * Time: 15:58
 */
class Example {
    public static <T extends Comparable<T>> T FindMax(T[] array) {
        T max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(max) > 0) {
                max = array[i];
            }
        }
        return max;
    }
}

public class Main3 {
    public static void main(String[] args) {
        Integer[] integers = {1, 3, 0, 18, 8};

        System.out.println(Example.<Integer>FindMax(integers));
    }
}
