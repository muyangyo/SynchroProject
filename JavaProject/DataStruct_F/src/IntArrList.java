import java.util.Arrays;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/29
 * Time: 21:17
 */
public class IntArrList {
    int UsedSize = 0;//使用了的大小
    int size = 5;//真实空间大小
    int[] arr = {0, 0, 0, 0, 0};//初始化

    public void MyIntArrUsedToString()//intarr数组打印(仅使用的)
    {
        System.out.printf("数组元素:");
        for (int i = 0; i < UsedSize; i++) {
            if (arr[i] != 0) {
                System.out.printf("%d ", arr[i]);
            }
        }
        System.out.println();
    }

    public static void posAdd(IntArrList exp1, int x) //在pos位置添加元素
    {

        System.out.println();
        System.out.printf("请输入位置:");
        ;
        int pos = 0;
        Scanner temp = new Scanner(System.in);
        pos = temp.nextInt();
        System.out.println();

        if (pos < 0 || pos >= exp1.UsedSize) {
            System.out.println("请输入有效地址!");
            posAdd(exp1, x);
        }
        if (exp1.UsedSize == exp1.size) {
            exp1.IsFullToEx(exp1);
        }
        for (int i = exp1.UsedSize - 1; i >= pos; i--) {
            exp1.arr[i + 1] = exp1.arr[i];
        }
        exp1.arr[pos] = x;
        exp1.UsedSize++;
    }

    public static void tail_add(IntArrList exp1, int x) //尾加
    {
        if (exp1.UsedSize == exp1.size) {
            exp1.IsFullToEx(exp1);
        }
        exp1.arr[exp1.UsedSize] = x;
        exp1.UsedSize++;
    }

    private void IsFullToEx(IntArrList exp1)//判断是否为满,满了自动扩容
    {
        size = size + 5;
        exp1.arr = Arrays.copyOf(exp1.arr, size);
    }

    public static void contains(IntArrList e1, int k)  // 判定是否包含某个元素
    {
        for (int i = 0; i < e1.UsedSize; i++) {
            if (e1.arr[i] == k) {
                System.out.println("找到了,下标为:" + i);
                return;
            }
        }
    }

    private void IntSubToString() //数组全长下标打印(无论使用与否)
    {
        System.out.println("==============================================");
        System.out.println("以下为数组和下标打印函数输出:");
        System.out.println("arr=" + Arrays.toString(arr));
        System.out.printf("sub=[");
        for (int i = 0; i < size; i++) {
            if (i != size - 1) {
                System.out.printf("%d, ", i);
            } else {
                System.out.printf("%d]\n", i);
            }
        }
    }

    public static void getPos(IntArrList e1) //得到pos位置的下标
    {
        int pos;
        System.out.println("请输入一个位置:");
        Scanner sc = new Scanner(System.in);
        pos = sc.nextInt();
        if (pos < 0 || pos >= e1.UsedSize) {
            System.out.println("位置错误!");
            getPos(e1);
        } else {
            System.out.println("该下标的元素:" + e1.arr[pos]);
        }
    }

    public static void setPos(IntArrList e1)  //给 pos 位置的元素设为 value
    {
        int pos;
        int val;
        Scanner exp1 = new Scanner(System.in);
        System.out.println("请输入位置:");
        pos = exp1.nextInt();
        if (pos < 0 || pos >= e1.UsedSize) {
            System.out.println("位置错误!");
            setPos(e1);
        }
        System.out.println("请输入值:");
        val = exp1.nextInt();
            e1.arr[pos] = val;
    }

    public static void remove(IntArrList e1)  //删除第一个对应的元素
    {
        int val;
        System.out.println("请输入一个需要删除的值:");
        Scanner sc = new Scanner(System.in);
        val = sc.nextInt();

        for (int i = 0; i < e1.UsedSize; i++) {
            if (e1.arr[i] == val) {
                int j = i;
                while (j < e1.UsedSize) {
                    e1.arr[j] = e1.arr[j + 1];
                    j++;
                }
                e1.UsedSize--;
                return;
            }
        }
        System.out.println("没有该值");
    }

    public int size()     // 获取顺序表长度
    {
        return UsedSize;
    }

    public void clear()     // 清空顺序表
    {
        UsedSize = 0;
    }

    public static void main(String[] args)  //内部测试接口
    {
        IntArrList e1 = new IntArrList();
        tail_add(e1, 1);
        tail_add(e1, 2);
        tail_add(e1, 3);
        e1.clear();
        //数组打印相关函数
        e1.MyIntArrUsedToString();
        e1.IntSubToString();
    }

}

