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

    public void remove(int x) {
        BinarySearchNode cur = root;
        BinarySearchNode parent = null;

        //寻找结点
        boolean flag = false;
        while (cur != null) {
            if (cur.val < x) {
                parent = cur;
                cur = cur.right;
            } else if (cur.val == x) {
                flag = true;
                break;//相同了,跳出
            } else {
                parent = cur;
                cur = cur.left;
            }

        }

        //跳出循环后,要不是为 null 要不是为 找到了
        if (flag) {

            //左边为 null 时(全为 null 时,也可以通过这个去解决)
            if (cur.left == null) {
                if (cur == root) {
                    root = cur.right;
                } else if (cur == parent.left) {
                    parent.left = cur.right;
                } else //cur == parent.right
                {
                    parent.right = cur.right;
                }
            }

            //右边为 null 时
            else if (cur.right == null) {
                if (cur == root) {
                    root = cur.left;
                } else if (cur == parent.left) {
                    parent.left = cur.left;
                } else //cur == parent.right
                {
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


    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        int[] arr = {5, 12, 3, 0, 8, 6, 10};
        for (int temp : arr) {
            tree.insert(temp);
        }

        tree.remove(12);
        System.out.println();
    }


}
