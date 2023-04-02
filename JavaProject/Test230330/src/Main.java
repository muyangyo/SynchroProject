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

    }

    public static void main1(String[] args) {
        int a = 10;
        Integer i = a; //自动装箱
        Integer ii = new Integer(a);  //显示装箱

        Integer b = 20;
        int out1 = b; //自动拆箱
        int out2 = b.intValue(); //显示拆箱
        float out3 = b.floatValue(); //显示拆箱(转不同类型)

    }
}
