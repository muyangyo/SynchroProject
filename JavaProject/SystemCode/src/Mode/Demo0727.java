package Mode;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/7/27
 * Time:15:01
 */

//懒汉版
class singletonLazy {
    private volatile static singletonLazy example = null;//先不实例化,等用到的时候进行实例化

    public static singletonLazy getInstance() {

        if (example == null) {
            synchronized (singletonLazy.class) {
                if (example == null) {
                    example = new singletonLazy();
                }
            }
        }
        return example;
    }

    private singletonLazy() {
    }//只能为私有,因为不能再实例化其他对象了(不符合单例了)
}

//饿汉版
class singleton {
    private static singleton example = new singleton();//直接随着 类加载 实例化

    private singleton() {
    } //只能为私有,因为不能再实例化其他对象了(不符合单例了)

    public static singleton getInstance() {
        return example;
    }
}

public class Demo0727 {
}
