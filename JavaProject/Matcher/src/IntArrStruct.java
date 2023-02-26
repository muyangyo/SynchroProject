import java.util.Arrays;
import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2022-11-22
 * Time: 21:34
 */
public class IntArrStruct {
    int usedsize = 0;//使用了的大小
    int size = 5;//真实空间大小
    int[] arr = new int[5];//初始化

    public  void tail_add(int x) //尾加
    {
        if (this.usedsize == this.size) {
            this.isfull_toex();
        }
        this.arr[this.usedsize] = x;
        this.usedsize++;
    }

    private void isfull_toex()//判断是否为满,满了自动扩容
    {
        size = size + 5;
        this.arr = Arrays.copyOf(this.arr, size);
    }

    public  boolean contains(int k)  // 判定是否包含某个元素
    {
        for (int i = 0; i < this.usedsize; i++) {
            if (this.arr[i] == k) {
                return true;
            }
        }
        return false;
    }


    public int size()     // 获取顺序表长度
    {
        return usedsize;
    }

    public void clear()     // 清空顺序表
    {
        usedsize = 0;
    }


}