/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/18
 * Time: 21:18
 */
public class demo1 {
    public static void main(String[] args) {
        Integer a = 500;
        Integer b = 500;
        System.out.println(a == b);
        System.out.println(a.equals(b));

        System.out.println(a.hashCode());
        System.out.println(b.hashCode());

        System.out.println("========================");

        Integer c = 120;
        Integer d = 120;
        System.out.println(c == d);
        System.out.println(c.equals(d));

        System.out.println(c.hashCode());
        System.out.println(d.hashCode());
    }
}
