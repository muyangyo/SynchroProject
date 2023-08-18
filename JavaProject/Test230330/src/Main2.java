/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/2
 * Time: 9:53
 */
public class    Main2 {
    public static void main(String[] args) {
        // 实例化泛型类
        GenericClass<Integer> genericClass = new GenericClass<>();

        // 设置pos位置的元素
        genericClass.setElement(2, 1);

        // 获取pos下标的元素
        System.out.println(genericClass.getElement(2));

        //现在没办法解决这个问题,等以后学到了再说
        Integer[] integers = genericClass.ToArray();
    }
}

class GenericClass<T> {
    private final Object[] array = new Object[10];

    public T getElement(int pos) {
        return (T) array[pos];
    }

    public void setElement(int pos, T element) {
        array[pos] = element;
    }


    /*现在这个会导致类型错误,因为编译后 <T> 都会自动转换为 object(因为没有限制,所以会优先转换为 object)
    而返回的Object数组里面,可能存放的是任何的数据类型,可能是String,可能是Person,运行的时候,
    直接转给Integer类型的数组，编译器认为是不安全的.*/
    public T[] ToArray() {
        return (T[]) this.array;
    }

}

