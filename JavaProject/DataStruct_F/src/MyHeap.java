import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/5/8
 * Time: 22:22
 */
public class MyHeap {
    public int[] Elem;
    public int UsedSize;

    public MyHeap() {
        this.Elem = new int[10];
    }

    public MyHeap(int[] ints) {
        this.Elem = Arrays.copyOf(ints, ints.length);
        UsedSize = ints.length;
    }

    public static MyHeap CreateBigHeap(int[] array) {
        MyHeap ret = new MyHeap(array);

        //调整子树
        int tail = ret.UsedSize - 1;//尾结点
        for (int parent = (tail - 1) / 2; parent >= 0; parent--) {
            ret.ShiftDown(parent, ret.UsedSize);
        }

        return ret;
    }

    private void ShiftDown(int parent, int usedSize) {
        int child = parent * 2 + 1;

        //判断是否有左子树,有才能进循环
        while (child < usedSize) {

            if (child + 1 < usedSize) //先判断有没有右树
            {
                if (Elem[child] < Elem[child + 1]) //如果右树有,且更大
                {
                    child = child + 1;
                }
            }

            if (Elem[child] > Elem[parent]) //子结点比父结点大
            {
                int temp = Elem[child];
                Elem[child] = Elem[parent];
                Elem[parent] = temp;
                /*
                由于是从最后一棵树开始调整的,如果我没动这棵树,我就不需要调整.
                就只需要调整我动了的那棵
                 */
                parent = child;
                child = parent * 2 + 1;
            }

            //如果没有比根大的子树,那这棵树就不需要动了
        }

    }

    public void push(int val) {
        //满了先扩容
        if (UsedSize == Elem.length) {
            Elem = Arrays.copyOf(Elem, Elem.length << 1);
        }

        //追加元素
        Elem[UsedSize] = val;
        //传入尾元素
        ShiftUp(UsedSize);
        UsedSize++;
    }

    private void ShiftUp(int child) {
        int parent = (child - 1) / 2;
        while (parent >= 0) {

            if (Elem[child] > Elem[parent]) //只要去比较父亲结点即可,因为其他的因为是大根堆了
            {
                int temp = Elem[child];
                Elem[child] = Elem[parent];
                Elem[parent] = temp;
                //向上走,这个是核心的区别
                child = parent;
                parent = (child - 1) / 2;
            } else {
                break; //只要有一次不比父结点大,那么就不可能比父结点的父结点大(原本就是大根堆)
            }

        }

    }

    public void poll() {
        if (isEmpty()) return;
        //由于队列只支持头删,所以先将头元素与尾元素互换一下,再整理
        int temp = Elem[0];
        Elem[0] = Elem[UsedSize - 1];
        Elem[UsedSize - 1] = temp;
        UsedSize--;
        //删完后对树进行调整,由于只有 0 树 变了,其他树仍是大根树,所以只要对 0 树进行调整一次
        ShiftDown(0, UsedSize);
    }

    public boolean isEmpty() {
        if (UsedSize == 0) return true;
        return false;
    }

    public int peek() {
        if (isEmpty()) return -1;
        System.out.println(Elem[0]);
        return Elem[0];
    }

    public static void main(String[] args) {
        int[] ints = {27, 15, 19, 18, 28, 34, 65, 49, 25, 37};
        MyHeap myHeap = CreateBigHeap(ints);
         
    }
}
