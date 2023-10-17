import java.util.Scanner;

public class Demo231017 {
    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            String[] strings = str.split(" ");
            boolean ry = isRunNian(strings[0]);
            int month = new Integer(strings[1]);
            int count = 0;
            switch (month - 1) {
                case 12:
                    count += 31;
                case 11:
                    count += 30;
                case 10:
                    count += 31;
                case 9:
                    count += 30;
                case 8:
                    count += 31;
                case 7:
                    count += 31;
                case 6:
                    count += 30;
                case 5:
                    count += 31;
                case 4:
                    count += 30;
                case 3:
                    count += 31;
                case 2:
                    if (ry) {
                        count += 29;
                    } else {
                        count += 28;
                    }
                case 1:
                    count += 31;
                default:
                    break;
            }
            int day = new Integer(strings[2]);
            count += day;
            System.out.println(count);
        }
    }

    private static boolean isRunNian(String str) {
        int year = new Integer(str);
        if (year % 4 == 0 && year % 100 != 0) {
            return true;
        } else if (year % 400 == 0) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n = in.nextInt();
            int[] ints = new int[n];
            for (int i = 0; i < n; i++) {
                ints[i] = in.nextInt();
            }


        }
    }
}
