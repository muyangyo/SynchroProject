package Mode;

//饿汉版
public class singleton {
    private static singleton example = new singleton();//直接随着 类加载 实例化

    private singleton() {
    } //只能为私有,因为不能再实例化其他对象了(不符合单例了)

    public static singleton getInstance() {
        return example;
    }
}