import java.util.Arrays;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/5/8
 * Time: 22:22
 */
public class MyHeap {
    public int[] elem;
    public int UsedSize;

    public MyHeap() {
        this.elem = new int[10];
    }

    public MyHeap(int[] ints) {
        this.elem = Arrays.copyOf(ints, ints.length);
        UsedSize = ints.length;
    }

    public static MyHeap CreateBigHeap(int[] array) {
        MyHeap ret = new MyHeap(array);
        //调整子树
        int tail = array.length - 1;//尾结点
        for (int parent = (tail - 1) / 2; parent >= 0; parent--) {

        }
    }


}
