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

    /*//根 -> 左 -> 右
    public static void 前序打印() {

    }

    //左 -> 根 -> 右
    public static void 中序打印() {

    }

    //左 -> 右 -> 根
    public static void 后续打印() {

    }*/

    public static BinaryNode CreateTree() {
        BinaryNode A = new BinaryNode('A');
        BinaryNode B = new BinaryNode('B');
        BinaryNode C = new BinaryNode('C');
        BinaryNode D = new BinaryNode('D');
        BinaryNode E = new BinaryNode('E');
        BinaryNode F = new BinaryNode('F');
        BinaryNode G = new BinaryNode('G');
        BinaryNode H = new BinaryNode('H');
        A.left = B;
        A.right = C;
        B.left = D;
        B.right = E;
        C.left = F;
        C.right = G;
        E.right = H;
        return A;
    } //创建了8个结点

    public static int SizeNull(BinaryNode root) {
        int ret = 0;
        if (root == null) {
            return 0;
        }
        ret++;
        ret += SizeNull(root.left);
        ret += SizeNull(root.right);
        return ret;
    }

    public static int Size(BinaryNode root) {
        if (root == null)
            return 0;
        return 1 + Size(root.right) + Size(root.left);
    }

    //求叶子结点数
    public static int CountForLeaf = 0;

    public static int LeafNodeCount(BinaryNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null)
            CountForLeaf++;
        LeafNodeCount(root.right);
        LeafNodeCount(root.left);
        return CountForLeaf;
    }

    public static int LeafNodeCountRet(BinaryNode root) {
        if (root == null) {
            return 0;
        }
        int count = 0;
        if (root.left == null && root.right == null)
            return 1;
        count += LeafNodeCountRet(root.right);
        count += LeafNodeCountRet(root.left);
        return count;
    }

    public static void main(String[] args) {
        BinaryNode root = BinaryTree.CreateTree();
        System.out.println(SizeNull(root));
        System.out.println(Size(root));
        System.out.println(LeafNodeCount(root));
        System.out.println(LeafNodeCountRet(root));
    }


}
