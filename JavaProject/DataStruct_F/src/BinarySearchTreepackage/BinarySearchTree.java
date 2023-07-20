package BinarySearchTreepackage;

/**
 * 创建于IntelliJ IDEA.
 * 描述:
 * User:MuYang
 * Date 2023/6/8
 * Time:9:40
 */

/*
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
     */
public class BinarySearchTree {
    static public class BinarySearchNode {
        public int val; //结点中存储的值
        public BinarySearchNode left; //结点的左子树
        public BinarySearchNode right; //结点的右子树

        public BinarySearchNode(int x) {
            val = x;
        }
    }

    public BinarySearchNode root; //根结点

    //搜索二叉搜索树中是否存在值为x的结点，如果存在则返回该结点，否则返回null
    public BinarySearchNode search(int x) {
        BinarySearchNode cur = root;

        while (cur != null) {
            if (cur.val == x) {
                return cur;
            } else if (cur.val < x) { //如果当前结点的值小于x，则去右子树中查找
                cur = cur.right;
            } else { //如果当前结点的值大于x，则去左子树中查找
                cur = cur.left;
            }
        }

        //cur 为 null 时，则说明找不到该结点
        return null;
    }

    //在二叉搜索树中插入一个值为x的结点
    public void insert(int x) {
        if (root == null) { //如果根结点为空，表明是第一次插入元素
            root = new BinarySearchNode(x);
        }
        BinarySearchNode prev = null;
        BinarySearchNode cur = root;

        //由于这是一棵二叉搜索树，所以不存在相同的元素插入，只能插入不同的元素
        while (cur != null) {
            if (cur.val < x) { //如果当前结点的值小于x，则插入到当前结点的右边
                prev = cur;
                cur = cur.right;
            } else if (cur.val == x) { //如果当前结点的值等于x，则不插入
                return;
            } else { //如果当前结点的值大于x，则插入到当前结点的左边
                prev = cur;
                cur = cur.left;
            }
        }

        //cur 为 null 时，则说明prev已经处于叶子结点的位置
        if (prev.val < x) {
            prev.right = new BinarySearchNode(x);
        } else {
            prev.left = new BinarySearchNode(x);
        }
    }

    //从二叉搜索树中删除一个值为x的结点
    public void remove(int x) {
        BinarySearchNode cur = root; //当前结点从根结点开始
        BinarySearchNode parent = null; //父节点初始化为空

        //寻找要删除的结点
        boolean found = false;
        while (cur != null) {
            if (cur.val < x) { //如果当前结点的值小于x，则去右子树中查找
                parent = cur;
                cur = cur.right;
            } else if (cur.val == x) { //如果当前结点的值等于x，则找到了要删除的结点
                found = true;
                break;
            } else { //如果当前结点的值大于x，则去左子树中查找
                parent = cur;
                cur = cur.left;
            }

        }

        //跳出循环后，如果found为true，则说明找到了要删除的结点，否则没有找到
        if (found) {

            //当左子树为空时
            if (cur.left == null) {
                if (cur == root) { //如果要删除的结点为根结点
                    root = cur.right;
                } else if (cur == parent.left) { //如果要删除的结点为父节点的左子树
                    parent.left = cur.right;
                } else { //如果要删除的结点为父节点的右子树
                    parent.right = cur.right;
                }
            }

            //当右子树为空时
            else if (cur.right == null) {
                if (cur == root) { //如果要删除的结点为根结点
                    root = cur.left;
                } else if (cur == parent.left) { //如果要删除的结点为父节点的左子树
                    parent.left = cur.left;
                } else { //如果要删除的结点为父节点的右子树
                    parent.right = cur.left;
                }
            }

            //左右都不为 null 时
            else {
                //叶子结点作为傀儡
                //想象这个是根结点,左右都不为空,我们去找右边的最小的替代掉根节点,才能满足左边都是小于根,右边的都是大于根的
                //右边的最左边的结点是右边最小的
                BinarySearchNode LeafPuppet = cur.right;
                BinarySearchNode LeafPuppetParent = cur;
                while (LeafPuppet.left != null) {
                    LeafPuppetParent = LeafPuppet;
                    LeafPuppet = LeafPuppet.left;
                }
                //此时 LeafPuppet 是在 最左边 的,那么它的左边就是 null,我们执行左删的情况即可
                //但是要先进行数据交换
                cur.val = LeafPuppet.val;
                //删除
                //注意有特殊情况,当要作为 傀儡的(LeafPuppet) 恰好是 要删除的(cur) 的右边时,要特殊出来
                if (LeafPuppetParent == cur) {
                    LeafPuppetParent.right = LeafPuppet.right;
                } else {
                    LeafPuppetParent.left = LeafPuppet.right; //如果不独立出来,则会将 不应该删除的左子树 直接覆盖掉了
                }


            }
        } else {
            System.out.println("没有该元素");
        }

    }


}
