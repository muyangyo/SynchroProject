import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.Arrays;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/30
 * Time: 19:06
 */
public class Main {
    public String SetString(String str) {
        char[] arr = new char[100];
        int PosForArr = 0;//等于UsedSize

        for (int i = 0; i < str.length(); i++) {
            if (SearchTheLetter(arr, str.charAt(i), PosForArr))
                continue;
            else {
                arr[PosForArr] = str.charAt(i);
                PosForArr++;
            }
        }

        return new String(arr, 0, PosForArr);
    }

    public static boolean SearchTheLetter(char[] arr, char exp, int UsedSize) {
        for (int i = 0; i < UsedSize; i++) {
            if (arr[i] == exp)
                return true;
        }
        return false;
    }

    public void merge(int A[], int m, int B[], int n) {
        int temp = m;
        for (int i = 0; i < n; i++) {
            A[temp] = B[i];
            temp++;
        }
        Arrays.sort(A);
    }

    public static void main(String[] args) {
        System.out.println(100 % 3.5);
    }
}
