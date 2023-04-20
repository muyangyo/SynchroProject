/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/16
 * Time: 15:46
 */
public class BinaryTree {
    static class BinaryNode {
        public char val;
        public BinaryNode left;
        public BinaryNode right;

        public BinaryNode(char data) {
            val = data;
        }
    }


    //根 -> 左 -> 右
    public static void 前序打印() {

    }

    //左 -> 根 -> 右
    public static void 中序打印() {

    }

    //左 -> 右 -> 根
    public static void 后续打印() {

    }

    public int size(BinaryNode root) {
        int ret = 0;
        if (root == null) {
            return 0;
        }
        ret++;
        ret += size(root.left);
        ret += size(root.right);
        return ret;
    }

    public static void main(String[] args) {

    }


}
