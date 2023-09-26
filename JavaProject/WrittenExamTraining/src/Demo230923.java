import java.sql.Array;
import java.util.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/23
 * Time: 21:12
 */
public class Demo230923 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            long M = in.nextLong();
            int N = in.nextInt();

            if (M > 0) {
                if (N < 10) {
                    List<Integer> list = new LinkedList<>();
                    while (M != 0) {
                        int temp = (int) M % N;
                        list.add(temp);
                        M /= N;
                    }
                    Collections.reverse(list);
                    for (int temp : list) {
                        System.out.print(temp);
                    }
                    System.out.print("\n");
                } else if (N == 10) {
                    System.out.println(M);
                } else {
                    //大于10的情况
                    StringBuilder stringBuilder = new StringBuilder();
                    while (M != 0) {
                        int temp = (int) M % N;
                        if (temp >= 10) {
                            switch (temp) {
                                case 10:
                                    stringBuilder.append("A");
                                    break;
                                case 11:
                                    stringBuilder.append("B");
                                    break;
                                case 12:
                                    stringBuilder.append("C");
                                    break;
                                case 13:
                                    stringBuilder.append("D");
                                    break;
                                case 14:
                                    stringBuilder.append("E");
                                    break;
                                case 15:
                                    stringBuilder.append("F");
                                    break;
                            }
                        } else
                            stringBuilder.append(temp);
                        M /= N;
                    }
                    stringBuilder.reverse();
                    System.out.println(stringBuilder);
                }
            } else {
                M = Math.abs(M);
                if (N == 2) {
                    List<Integer> list = new LinkedList<>();
                    while (M != 0) {
                        int temp = (int) M % N;
                        list.add(temp);
                        M /= N;
                    }
                    Collections.reverse(list);
                    int count = 32 - list.size();
                    boolean first = true;
                    for (; count > 0; count--) {
                        if (first) {
                            System.out.print("1");
                            first = false;
                        } else System.out.print("0");
                    }
                    for (int temp : list) {
                        System.out.print(temp);
                    }
                    System.out.print("\n");

                } else if (N < 10) {
                    List<Integer> list = new LinkedList<>();
                    while (M != 0) {
                        int temp = (int) M % N;
                        list.add(temp);
                        M /= N;
                    }
                    Collections.reverse(list);
                    System.out.print("-");
                    for (int temp : list) {
                        System.out.print(temp);
                    }
                    System.out.print("\n");
                } else if (N == 10) {
                    System.out.print("-");
                    System.out.println(M);
                } else {
                    //大于10的情况
                    StringBuilder stringBuilder = new StringBuilder();
                    while (M != 0) {
                        int temp = (int) M % N;
                        if (temp >= 10) {
                            switch (temp) {
                                case 10:
                                    stringBuilder.append("A");
                                    break;
                                case 11:
                                    stringBuilder.append("B");
                                    break;
                                case 12:
                                    stringBuilder.append("C");
                                    break;
                                case 13:
                                    stringBuilder.append("D");
                                    break;
                                case 14:
                                    stringBuilder.append("E");
                                    break;
                                case 15:
                                    stringBuilder.append("F");
                                    break;
                            }
                        } else
                            stringBuilder.append(temp);
                        M /= N;
                    }
                    stringBuilder.reverse();
                    System.out.print("-");

                    System.out.println(stringBuilder);
                }

            }

        }


    }


    public static void main3(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            long count1 = in.nextInt();
            long count2 = in.nextInt();
            long count3 = in.nextInt();
            long count4 = in.nextInt();
            float A = (float) ((count1 + count3) / 2);
            float B = (float) ((count2 + count4) / 2);
            float C = (float) (count4 - B);
            if ((A < 0 || B < 0 || C < 0)) System.out.println("No");
            else System.out.println(A + " " + B + " " + C);
        }
    }


    public static void main1(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            long[] arr = new long[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextLong();
            }

            int fast = 1;
            int slow = 0;
            int count = 1;

            boolean increase = false;
            boolean decrease = false;
            while (fast < n) {
                if (arr[fast] > arr[slow]) increase = true;
                else if (arr[fast] == arr[slow]) ;
                else decrease = true;

                if (increase && decrease) {
                    count++;
                    decrease = false;
                    increase = false;
                }
                fast++;
                slow++;
            }
            System.out.println(count);

        }
    }

    /*//简易实现
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            String[] strs = str.split(" |\n");
            for (int i = strs.length - 1; i >= 0; i--) {
                System.out.print(strs[i] + " ");
            }
        }
    }*/


    public static void main2(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            int fast = 0;
            int slow = 0;
            char[] chars = str.toCharArray();
            List<String> list = new LinkedList<>();
            StringBuilder stringBuilder;
            while (fast < chars.length) {
                if (chars[fast] == ' ') {
                    stringBuilder = new StringBuilder();
                    while (slow <= fast) {
                        stringBuilder.append(chars[slow]);
                        slow++;
                    }
                    list.add(stringBuilder.toString());
                }
                fast++;
            }
            stringBuilder = new StringBuilder();
            while (slow < fast) {
                stringBuilder.append(chars[slow]);
                slow++;
            }
            list.add(stringBuilder.toString());
            Collections.reverse(list);
            boolean first = true;
            for (String temp : list) {
                if (first) {
                    System.out.print(temp);
                    first = false;
                } else
                    System.out.print(temp + " ");
            }

        }
    }


}

