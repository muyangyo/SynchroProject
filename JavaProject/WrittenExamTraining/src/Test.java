/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/13
 * Time: 18:51
 */


public class Test {
    String string;


    public static void main(String[] args) {
        /*System.out.println(new B().getValue());
        int a;
        a = 10;
        System.out.println(a);
        String str;
        //str = "123";
        //System.out.println(str);
        System.out.println(new Test().string);*/

        int i = 0;
        Integer j = new Integer(0);
        System.out.println(i == j);
        System.out.println(j.equals(i));// i 这里自动装箱了

    }

    static class A {
        protected int value;

        public A(int v) {
            setValue(v);
        }

        public void setValue(int value) {
            this.value = value;   //5
        }

        public int getValue() {
            try {
                value++;     //6
                return value;
            } catch (Exception e) {
                System.out.println(e.toString());
            } finally {
                this.setValue(value);   //6
                System.out.println(value);    //6
            }
            return value;
        }
    }

    static class B extends A {
        public B() {
            super(5);
            setValue(getValue() - 3);         //3
        }

        public void setValue(int value) {
            super.setValue(2 * value);       //6
        }
    }
}

