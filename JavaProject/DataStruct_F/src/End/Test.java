package End;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


@FunctionalInterface //函数式接口: 当一个接口当中,只有 一个 抽象方法的时候(其他类型的方法不管)
//无返回值 无参数
interface NoParameterNoReturn {
    void test();

    default void Fun() {
        System.out.println("其他方法");
    }
}

//无返回值 多个参数
@FunctionalInterface
interface MoreParameterNoReturn {
    void test(int a, int b);
}

//有返回值 多参数
@FunctionalInterface
interface MoreParameterReturn {
    int test(int a, int b);
}


public class Test {

    public static void lambdaForCollection() {
        ArrayList<String> list = new ArrayList<>();
        list.add("hello");
        list.add("abcd");
        list.add("efgh");

        //普通写法
        /*list.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });*/
        //使用lambda表达式写法
        list.forEach(s -> {
            System.out.println(s);
        });

        System.out.println("=====================================");
        //普通写法
        /*list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });*/
        //使用lambda表达式写法
        list.sort(
                (o1, o2) -> {
                    return o1.compareTo(o2);
                }
        );

        //用lambda表达式简化写法
//        list.sort(
//                (o1, o2) -> o1.compareTo(o2)
//        );

        list.forEach(s -> {
            System.out.println(s);
        });
    }



    public static void problem() {
        int a = 10;
        //a = 1999;
        NoParameterNoReturn noParameterNoReturn = new NoParameterNoReturn() {
            @Override
            public void test() {
                //此时捕获的变量 一定是没有修改过的  或者是一个常量
                System.out.println("a = " + a);
            }
        };

        noParameterNoReturn.test();

        //此时捕获的变量 一定是没有修改过的  或者是一个常量
        NoParameterNoReturn noParameterNoReturn2 = () -> System.out.println("a = " + a);
        noParameterNoReturn2.test();
    }

    public static void main(String[] args) {
        lambdaForCollection();
    }

    public static void main1(String[] args) {

        //默认实现方式
        MoreParameterReturn moreParameterReturn = new MoreParameterReturn() {
            @Override
            public int test(int a, int b) {
                return a + b;
            }
        };
        System.out.println(moreParameterReturn.test(1, 2));

        System.out.println("========================");

        //lambda实现方式
        MoreParameterReturn moreParameterReturn1 = (x, y) -> {
            return x + y;
        };
        System.out.println(moreParameterReturn1.test(1, 2));

        //lambda语法简化实现方式
        MoreParameterReturn moreParameterReturn2 = (x, y) -> x + y;
        System.out.println(moreParameterReturn2.test(1, 2));

    }
}
