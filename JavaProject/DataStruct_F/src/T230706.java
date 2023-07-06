import java.util.List;
import java.util.SplittableRandom;

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
    public static void main(String[] args) {
        Item<String> item = new Item<>();
        item.setInfo("你好");
        fun(item);
    }

    public static <T> void fun(Item<T> temp) {
        System.out.println(temp.getInfo());
    }

    // TODO: 2023/7/6 问完问题后,完结通配符的语法
    // super 一般用于 写入操作
    public void addToList(List<? super Number> list) {
        list.add(10);
        list.add(1.0f);
        list.add(1.02);
    }

    // extend 一般用于 读取操作
    public void printList(List<? extends Integer> list) {
        for (Number num : list) {
            System.out.println(num);
        }
    }

}
