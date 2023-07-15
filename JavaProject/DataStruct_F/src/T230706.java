import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/6
 * Time:21:25
 */
class Item<T> {
    public T info;

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }
}

public class T230706 {

    // super 一般用于 写入操作
    public static void addToList(List<? super Number> list)  //注意:只能接收 Number 本身或其父类
    {
        list.add(10);//向上转型
        list.add(1.0f);//向上转型
        list.add(1.02);//向上转型
        System.out.println(list);//当然也可以读取,但是编译器会认为这是一个 object 对象
        //即发生了向上转型,但是内存里开辟的空间依旧是 子类本身
    }

    // extend 只用于 读取操作
    public static void printList(List<? extends Number> list) //注意:只能接收 Number 本身或其子类
    {
        for (Number num : list) {
            System.out.println(num);//读取的时候,编译器会默认全为 Number,即会发生向上转型,但是内存里开辟的空间依旧是 子类本身
            //所以读取没有问题,但是写入就不行
        }
        //list.add(1.0);//编译器认为这是不安全操作,因为如果我传过来的是一个 Integer,那我的空间里根本不可能存放 Double 类型
        //但是如果我传过来的是一个 Number,那发生了 向上转型 那就可以存放
        //但是编译器并不知道你传的是哪个,所以为了安全,统一认为这是错误的
    }

    public static void main(String[] args) {
        int i = 0;
        Integer j = new Integer(0);
        System.out.println(i == j);
        System.out.println(j.equals(i));
    }
}
