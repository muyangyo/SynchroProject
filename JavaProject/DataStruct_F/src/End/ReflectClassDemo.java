package End;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectClassDemo {

    public static void reflectNewInstance() {
        try {
            Class<?> c1 = Class.forName("End.Student");//根据 类名 返回类的对象(以src为起始地址,
            // 如果在其他文件夹内则需要相对于src的相对地址
            Student student = (Student) c1.newInstance();//创建类的实例
            System.out.println(student);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reflectPrivateConstructor() {
        try {
            Class<?> c1 = Class.forName("End.Student");//获取 className 对应的类
            //获取含有 String 和 int 参数的构造方法
            Constructor<?> declaredConstructorStudent = c1.getDeclaredConstructor(String.class, int.class);
            //Constructor<?> declaredConstructorStudent = c1.getConstructor(); // 获取无参的构造方法

            declaredConstructorStudent.setAccessible(true);//使 私有的 转变为 可访问的
            Student student = (Student) declaredConstructorStudent.newInstance("god", 15);//使用获取到的构造方法实例化
            //Student student = (Student) declaredConstructorStudent.newInstance();

            System.out.println("使用私有构造方法new的对象" + student);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void reflectPrivateField() {
        try {
            Class<?> c1 = Class.forName("End.Student");//获取类名
            Student student = (Student) c1.newInstance();//实例化对象
            Field field = c1.getDeclaredField("name");//获取对应 name 的成员变量
            field.setAccessible(true);//使 私有的 转变为 可访问的
            //开始修改
            field.set(student, "aoe");//修改对象的 field 所获取到的 成员变量
            System.out.println(student);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reflectPrivateMethod() {
        try {
            Class<?> c1 = Class.forName("End.Student");//获取 className 对应的类
            Student student = (Student) c1.newInstance();//实例化对象

            Method method = c1.getDeclaredMethod("function", String.class);//获取名字为 name 和含有 String 参数的方法
            method.setAccessible(true);//使 私有的 转变为 可访问的
            method.invoke(student, "我是一个参数！");//调用 function方法
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        reflectPrivateConstructor();
    }

}