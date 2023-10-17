import jdk.jfr.internal.Utils;

import javax.rmi.CORBA.Util;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/15
 * Time: 10:10
 */
public class Demo231015 {
    public int binInsert(int n, int m, int j, int i) {
        m = m << j;
        n = n ^ m;
        return n;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();

            //本身除下来就可能是一个奇数,而且是一个素数 比如: 4/2=2
            if (isSuShu(n / 2)) {
                System.out.println(n / 2);
                System.out.println(n / 2);
                continue;
            }

            int decrease = n / 2 - 1;
            int increase = n / 2 + 1;
            while (true) {
                if (isSuShu(increase) && isSuShu(decrease)) break;
                decrease--;//不能直接 -=2 ,比如 54/2=27,其 27-1=26 , 27+1=28 ,就变成偶数了,偶数是不可能有素数的(除了2)
                increase++;
            }

            System.out.println(decrease);
            System.out.println(increase);
        }

    }

    private static boolean isSuShu(int x) {
        for (int i = 2; i < x / 2; i++) {
            if (x % i == 0) return false;
        }
        return true;
    }
}
