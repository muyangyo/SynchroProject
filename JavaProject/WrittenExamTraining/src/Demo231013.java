import java.util.Scanner;

public class Demo231013 {
    public boolean checkWon(int[][] board) {
        if ((board[0][0] + board[1][1] + board[2][2] == 3) || (board[0][2] + board[1][1] + board[2][0] == 3))
            return true;

        for (int i = 0; i < 3; i++) {
            //行判断
            if ((board[i][0] + board[i][1] + board[i][2] == 3)) return true;
            //列判断
            if (board[0][i] + board[1][i] + board[2][i] == 3) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String password = in.nextLine();
            int count = 0;

            //长度判断
            int length = password.length();
            if (length <= 4) count += 5;
            else if (length <= 7) count += 10;
            else count += 25;

            boolean dZM = false;//大字母判断
            boolean xZM = false;//小字母判断
            //boolean zM = false;//包含大小写字母
            boolean fH = false;//符号
            boolean sFH = false;//多符号
            boolean sZ = false;//数字
            boolean sSZ = false;//多数字
            char[] chars = password.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                int temp = (int) chars[i];
                //字母判断
                if (temp >= 'a' && temp <= 'z') xZM = true;
                if (temp >= 'A' && temp <= 'Z') dZM = true;
                //数字判断
                if (temp >= '0' && temp <= '9') {
                    if (sZ) sSZ = true;
                    sZ = true;
                }
                //符号判断
                if ((temp >= '!' && temp <= '/')
                        || (temp >= ':' && temp <= '@')
                        || (temp >= '[' && temp <= '`')
                        || (temp >= '{' && temp <= '~')
                ) {
                    if (fH) sFH = true;
                    fH = true;
                }
            }
            if (dZM && xZM) count += 20;
            else if (dZM || xZM) count += 10;

            if (sSZ) count += 20;
            else if (sZ) count += 10;

            if (sFH) count += 25;
            else if (fH) count += 10;

            if (dZM && xZM && sZ && fH) count += 5;
            else if ((sZ && fH && dZM) || (sZ && fH && xZM)) count += 3;
            else if ((sZ && xZM) || (sZ && dZM)) count += 2;

            if (count >= 90) System.out.println("VERY_SECURE");
            else if (count >= 80) System.out.println("SECURE");
            else if (count >= 70) System.out.println("VERY_STRONG");
            else if (count >= 60) System.out.println("STRONG");
            else if (count >= 50) System.out.println("AVERAGE");
            else if (count >= 25) System.out.println("WEAK");
            else System.out.println("VERY_WEAK");
        }
    }
}
