import sun.reflect.generics.tree.Tree;

import java.util.Collection;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/13
 * Time: 17:03
 */
public class Demo231013_1 {

    public int getLCA(int a, int b) {
        while (a != b) {
            if (a > b) {
                a /= 2;
            } else {
                b /= 2;
            }
        }
        return a;
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int count = 0;
            int temp = 0;
            for (int i = 0; i < 32 && n != 0; i++) {
                if ((n >>> i & 1) == 1) {
                    temp++;
                    if (temp > count) count = temp;
                } else temp = 0;
            }
            System.out.println(count);
        }
    }
}
