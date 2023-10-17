import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/12
 * Time: 15:30
 */
public class Demo231012 {
    //另类加法: https://www.nowcoder.com/questionTerminal/e7e0d226f1e84ba7ab8b28efc6e1aebc?toCommentId=31716
    public int addAB(int A, int B) {
        int count = A ^ B;//如果不考虑进位的,异或就是相加
        int carry = A & B;//不考虑其余的,会产生进位的二进制
        while (carry != 0) {
            A = count;
            B = carry << 1;

            count = A ^ B;
            carry = A & B;
        }
        return count;
    }

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
