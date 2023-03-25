package Blog230325;

interface IEg {
    int a = 10;

    public static final int b = 20;

    public default void fun1() {
        System.out.println("接口中的default(普通)方法fun1");
    }

    public static void fun2S() {
        System.out.println("接口中的静态方法fun2S");
    }

    void method1();

    public abstract void method2();
}


class Temp implements IEg {
    @Override
    public void method1() {
        System.out.println("method1");
    }

    @Override
    public void method2() {
        System.out.println("method2");
    }
}

public class Test1 implements IEg, IEgE {
    @Override
    public void method1() {
        System.out.println("继承来的method1");
    }

    @Override
    public void method2() {
        System.out.println("继承来的method2");
    }

    public static void main(String[] args) {
        System.out.println(IEg.a);
        IEg.fun2S();

        Temp temp =new Temp();
        temp.method1();
        temp.method2();
        temp.fun1();

    }
}

interface IEgE extends IEg {
}

