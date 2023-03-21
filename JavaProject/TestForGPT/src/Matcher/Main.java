package Matcher;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/21
 * Time: 17:56
 */
public class Main {
    
    public static boolean Rules(String e1, String e2) {
        if (e1.equals("高朝旭") && e2.equals("韩晴") || e1.equals("韩晴") && e2.equals("高朝旭")) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Matcher matcher = new Matcher("柳俊杰 李绍勇 胡浩 杨振宇 高朝旭 王华浩 李均文 张俊威 张政 韩晴 钟靖 刘庆彬");
    }
}
