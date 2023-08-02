package Mode;

//懒汉版
public class singletonLazy {
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