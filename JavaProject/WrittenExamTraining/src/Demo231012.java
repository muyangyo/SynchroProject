import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/12
 * Time: 15:30
 */
public class Demo231012 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[][] ints = new int[10][10];
        for (int i = 0; i < 10; i++) {
            ints[0][i] = 1;
            ints[i][0] = 1;
        }
        ints[0][0] = 0;
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                ints[i][j] = ints[i - 1][j] + ints[i][j - 1];
            }
        }
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int m = in.nextInt();
            System.out.println(ints[n][m]);
        }
    }

}
