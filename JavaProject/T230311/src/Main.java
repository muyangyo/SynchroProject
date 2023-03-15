/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/11
 * Time: 23:33
 */
public class Main {
    public static void printArray(int[] ints) {
        for (int anInt : ints) {
            System.out.print(anInt + " ");
        }
        System.out.println();
    }

    public static void transform(int[] ints) {
        for (int i = 0; i < ints.length; i++) {
            ints[i] = ints[i] * 2;
        }
    }

    public static void sum(int[] ints) {
        int sum = 0;
        for (int anInt : ints) {
            sum += anInt;
        }
        System.out.println(sum);
    }

    public static double avg(int[] ints) {
        int sum = 0;
        for (int anInt : ints) {
            sum += anInt;
        }
        return (double) sum / ints.length;
    }

    public static int[] OddFEvenB(int[] ints) {
        int[] Odds = new int[ints.length]; //奇数
        int PosForOdd = 0;
        int[] Evens = new int[ints.length]; //偶数
        int PosForEven = 0;
        for (int anInt : ints) {
            if (anInt % 2 == 0) {
                Evens[PosForEven] = anInt;
                PosForEven++;
            } else {
                Odds[PosForOdd] = anInt;
                PosForOdd++;
            }
        }
        for (int i = 0; i < PosForEven; i++) {
            Odds[PosForOdd] = Evens[i];
            PosForOdd++;
        }
        return Odds;
    }


    //int是该数的下标
    public static int BinarySearch(int[] ints, int x) {
        int z = 0;
        int y = ints.length - 1;
        while (z <= y) {
            int mid = (z + y) / 2;
            if (ints[mid] < x) {
                z = mid + 1;
            } else if (ints[mid] > x) {
                y = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public static void Bob(int[] ints) {
        for (int i = 0; i < ints.length - 1; i++) {
            for (int j = 0; j < ints.length - 1 - i; j++) {
                if (ints[j] > ints[j + 1]) {
                    int temp = ints[j];
                    ints[j] = ints[j + 1];
                    ints[j + 1] = temp;
                }
            }
        }
    }


    public static void TDS(int x, int[] ints) {
        if (ints == null) {
            System.out.println("空数组你找啥???");
        }
        int pos1 = 0;
        int pos2 = 1;
        while (pos1 != ints.length) {
            while (pos2 != ints.length) {
                if (ints[pos1] + ints[pos2] == x) {
                    System.out.println("[" + pos1 + "," + pos2 + "]");
                    return;
                }
                pos2++;
            }
            pos1++;
            pos2 = pos1 + 1;
        }
        System.out.println("没有找到!");
    }

    public static void FindTheSingle(int[] ints) {
        int temp = 0;
        for (int anInt : ints) {
            temp ^= anInt;
        }
        System.out.println(temp);
    }

    public static void FindTheNumberTheMost(int[] ints) {
        int[] NumberofOccurrences = new int[ints.length];
        int PosForOc = 0;

        int i = 0;
        while (i < ints.length) {
            int Number = ints[i];
            int count = 0;
            for (int temp : ints) {
                if (temp == Number) {
                    count++;
                }
            }
            NumberofOccurrences[PosForOc] = count;
            i++;
            PosForOc = i;
        }
        int SubMax = 0;
        for (int j = 1; j < NumberofOccurrences.length - 1; j++) {
            if (NumberofOccurrences[j] > NumberofOccurrences[SubMax]) {
                SubMax = j;
            }
        }
        System.out.println("出现次数多的数:" + ints[SubMax]);
    }

    public static boolean ThreeConsecutiveOddNumbers(int[] ints) {
        int pos = 1;
        while (pos < ints.length - 1) {
            if ((ints[pos - 1] * ints[pos] * ints[pos + 1]) % 2 != 0) {
                return true;
            }
            pos++;
        }
        return false;
    }

    public static void main(String[] args) {
        
    }


}
