package Blog230325;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/25
 * Time: 15:56
 */

abstract class Shape {
    int a;
    static int b;

    public void Fun1() {
        System.out.println("抽象类中的普通方法");
    }

    public static void FunS() {
        System.out.println("抽象类中的静态方法");
    }

    public abstract void Draw();

}

class Flower extends Shape {
    @Override
    public void Draw() {
        System.out.println("❀");
    }
}

class Cycle extends Shape {
    @Override
    public void Draw() {
        System.out.println("○");
    }
}

class Triangle extends Shape {
    @Override
    public void Draw() {
        System.out.println("△");
    }
}

public class Main {
    public static void main(String[] args) {
        //这里利用了向上转型
        Shape[] shapes = {new Cycle(), new Flower(), new Triangle()};
        for (Shape temp : shapes) {
            temp.Draw();
        }
    }
}