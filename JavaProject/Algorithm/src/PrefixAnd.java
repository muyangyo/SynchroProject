import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/7
 * Time: 13:45
 */
public class PrefixAnd {
    //前缀和: https://www.nowcoder.com/practice/acead2f4c28c401889915da98ecdc6bf?tpId=230&tqId=2021480&ru=/exam/oj&qru=/ta/dynamic-programming/question-ranking&sourceUrl=%2Fexam%2Foj%3Fpage%3D1%26tab%3D%25E7%25AE%2597%25E6%25B3%2595%25E7%25AF%2587%26topicId%3D196
    public static void PrefixesAndTemplates(String[] args) {
        //获取数据
        Scanner scanner = new Scanner(System.in);
        int length = scanner.nextInt();
        int searchH = scanner.nextInt();
        long[] arr = new long[length];
        for (int i = 0; i < length; i++) {
            arr[i] = scanner.nextInt();
        }
        //创建 pd 数组
        long[] pd = new long[length + 1];
        pd[0] = 0;
        for (int i = 1; i < length + 1; i++) {
            pd[i] = pd[i - 1] + arr[i - 1];
        }
        while (searchH != 0) {
            searchH--;
            int left = scanner.nextInt();
            int right = scanner.nextInt();
            System.out.println(pd[right] - pd[left - 1]);
        }
    }
}
