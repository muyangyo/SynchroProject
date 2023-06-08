/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/6/8
 * Time:9:40
 */
public class BinarySearchTree {
    static class BinarySearchNode {
        int val;
        BinarySearchNode left;
        BinarySearchNode right;

        public BinarySearchNode(int x) {
            val = x;
        }
    }

    BinarySearchNode root;

    public BinarySearchNode search(int x, BinarySearchNode cur) {
        //回返条件
        if (cur == null) return null;

        BinarySearchNode rRet = null;//左返回结点
        BinarySearchNode lRet = null;//右返回结点
        if (cur.val == x) return cur;
        else if (cur.val < x) {
            rRet = search(x, cur.right);
        } else {
            lRet = search(x, cur.left);
        }

        return rRet == null ? lRet : rRet;
    } //递归实现

    public BinarySearchNode search(int x) {
        BinarySearchNode cur = root;

        while (cur != null) {
            if (cur.val == x) {
                return cur;
            } else if (cur.val < x) {
                cur = cur.right;//左边数据都比根大
            } else {
                cur = cur.left;//右边数据都比根小
            }
        }

        //cur 为 null 时,则说明找不到该结点
        return null;
    }

    public void insert(int x) {
        if (root == null) //第一次插入元素
        {
            root = new BinarySearchNode(x);
        }
        BinarySearchNode prev = null;
        BinarySearchNode cur = root;

        //由于这是一棵二叉搜索树,所以不存在相同的元素插入,只能插入不同的元素
        while (cur != null) {
            if (cur.val < x) {
                prev = cur;
                cur = cur.right;
            } else if (cur.val == x) {
                return;//如果相同,直接返回结束即可
            } else {
                prev = cur;
                cur = cur.left;
            }
        }

        //cur 为 null 时,则说明 prev 已经处于 叶子结点 的位置
        if (prev.val < x) prev.right = new BinarySearchNode(x);
        else prev.left = new BinarySearchNode(x);
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        int[] arr = {5, 12, 3, 0, 8, 6, 10};
        for (int temp : arr) {
            tree.insert(temp);
        }

        System.out.println();
    }
}
