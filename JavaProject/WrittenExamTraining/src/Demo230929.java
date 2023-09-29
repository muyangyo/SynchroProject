import java.math.BigInteger;
import java.util.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/9/29
 * Time: 10:18
 */
public class Demo230929 {
    public static int StrToInt(String str) {
        char[] chars = str.toCharArray();
        int index = 0;
        long ret = 0;
        boolean first = true;
        boolean second = true;
        boolean fh = true;//true为正,false为负
        List<Integer> list = new LinkedList<>();
        for (; index < chars.length; index++) {
            int temp = (int) chars[index];
            if (temp >= '0' && temp <= '9') {
                if (first) {
                    if (temp == (int) '0') return 0;
                } else if (!first && second) {
                    if (temp == (int) '0') return 0;
                    second = false;
                }
                list.add((temp - '0'));
            } else {
                if (first) {
                    first = false;
                    if (temp == '+') {
                        fh = true;
                    } else if (temp == '-') {
                        fh = false;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        }
        int bit = list.size();
        for (int temp : list) {
            ret += pow(temp, bit--);
        }
        if (fh) return (int) ret;
        else return (int) (-ret);
    }

    private static int pow(int temp, int size) {
        size--;
        for (int i = 0; i < size; i++) {
            temp *= 10;
        }
        return temp;
    }

    public static void main(String[] args) {
        System.out.println(StrToInt("+2147483647"));
    }


    static class Node {
        int x;
        int y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int W = in.nextInt();
            int H = in.nextInt();
            int ret = 1;
            List<Node> list = new ArrayList<>();
            for (int i = 0; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    Node temp = new Node(i, j);
                    boolean flag = true;
                    for (Node node : list) {
                        if (!mathDistance(node, temp)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        ret++;
                        list.add(temp);
                    }
                }
            }
            System.out.println(ret);

        }
    }

    public static boolean mathDistance(Node node1, Node node2) {
        int x1 = node1.x;
        int y1 = node1.y;

        int x2 = node2.x;
        int y2 = node2.y;
        return ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) != 2;
    }

}
