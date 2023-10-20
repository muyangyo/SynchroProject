import java.util.Arrays;
import java.util.Scanner;

public class Demo231020 {
    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            str = str.toUpperCase();
            String a = in.nextLine();
            a = a.toUpperCase();
            char aChar = a.charAt(0);

            char[] chars = str.toCharArray();
            int count = 0;
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == aChar) count++;
            }
            System.out.println(count);
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int limitH = 150;
        int limitL = limitH * 2 - 1;
        long[][] ints = new long[limitH][limitL];
        ints[0][limitL / 2] = 1;
        for (int h = 1; h < limitH; h++) {
            for (int l = 0; l < limitL; l++) {
                if (l - 1 < 0) ints[h][l] = ints[h - 1][l] + ints[h - 1][l + 1];
                else if (l + 1 > limitL - 1) ints[h][l] = ints[h - 1][l] + ints[h - 1][l - 1];
                else ints[h][l] = ints[h - 1][l] + ints[h - 1][l - 1] + ints[h - 1][l + 1];
            }
        }
        while (in.hasNextInt()) {
            int n = in.nextInt();
            boolean flag = true;
            int index = 0;
            for (int i = 0; i < limitL; i++) {
                long temp = ints[n - 1][i];
                if ((temp != 0)) {
                    index++;
                    if (temp % 2 == 0) {
                        System.out.println(index);
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                System.out.println(-1);
            }
        }
    }
}
