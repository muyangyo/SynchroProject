import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    //前序打印:根 -> 左 -> 右
    public static void preOrder(BinaryNode root) {
        if (root == null) return;
        System.out.print(root.val + " ");
        preOrder(root.left);
        preOrder(root.right);
    }

    /*public static List<Integer> ret = new ArrayList<>();

    public static List<Integer> preOrder1(BinaryNode root) {
        if (root == null) return null;
        ret.add(root.val);
        preOrder1(root.left);
        preOrder1(root.right);
        return ret;
    }*/


    //中序打印:左 -> 根 -> 右
    public static void inOrder(BinaryNode root) {
        if (root == null) return;
        inOrder(root.left);
        System.out.println(root.val);
        inOrder(root.right);
    }

    //后序打印:左 -> 右 -> 根
    public static void postOrder(BinaryNode root) {
        if (root == null) return;
        postOrder(root.left);
        postOrder(root.right);
        System.out.println(root.val);
    }

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


    //算结点数
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
        if (root == null) return 0;
        return 1 + Size(root.right) + Size(root.left);
    }

    //求叶子结点数
    public static int CountForLeaf = 0;

    public static int LeafNodeCount(BinaryNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) CountForLeaf++;
        LeafNodeCount(root.right);
        LeafNodeCount(root.left);
        return CountForLeaf;
    }

    public static int LeafNodeCountRet(BinaryNode root) {
        if (root == null) {
            return 0;
        }
        int count = 0;
        if (root.left == null && root.right == null) return 1;
        count += LeafNodeCountRet(root.right);
        count += LeafNodeCountRet(root.left);
        return count;
    }

    /*
    新
     */

    //获取第 K 层的结点个数
    public static int getKLevelNodeCount(BinaryNode root, int k) {
        int ret = 0;
        if (root == null) {
            return 0;
        }
        if (k == 1) {
            return 1;
        }
        ret += getKLevelNodeCount(root.left, k - 1);
        ret += getKLevelNodeCount(root.right, k - 1);
        return ret;
    }

    //获取树的高度
    public static int getHeight(BinaryNode root) {
        if (root == null) {
            return 0;
        }
        int LHeight = getHeight(root.left);
        int RHeight = getHeight(root.right);
        return 1 + (LHeight > RHeight ? LHeight : RHeight);
    }

    //寻值(前序遍历法)
    public static BinaryNode FindValue_pro(BinaryNode root, char key) {
        if (root == null) {
            return null;
        }
        if (root.val == key) {
            return root;
        }
        BinaryNode retL = FindValue_pro(root.left, key);
        BinaryNode retR = FindValue_pro(root.right, key);
        return retL == null ? retR : retL;
    }

    //相同的树
    public boolean isSameTree(BinaryNode p, BinaryNode q) {
        //先对根判断避免为空的情况
        if (p == null && q == null) {
            return true;
        }
        if (p == null && q != null || p != null && q == null) {
            return false;
        }

        //对根判值
        if (p.val != q.val) {
            return false;
        }

        //再对左右树进行判断
        boolean flagL = false;
        flagL = isSameTree(p.left, q.left);
        if (flagL == false) return false;

        boolean flagR = false;
        flagR = isSameTree(p.right, q.right);
        if (flagR == false) return false;

        return true;
    }

    //是否为子树
    public boolean isSubtree(BinaryNode root, BinaryNode subRoot) {
        if (root == null)//空树是不可能为任何树的父树的
            return false;
        if (subRoot == null)//空树是任何树的子树
            return true;
        if (isSameTree(root, subRoot))//判断当前这棵树是否可能是子树
            return true;

        //判断子树是否可能为子树
        if (isSubtree(root.left, subRoot)) return true;
        if (isSubtree(root.right, subRoot)) return true;
        return false;
    }

    //翻转树
    public static BinaryNode invertTree(BinaryNode root) {
        if (root == null) return null;
        BinaryNode TempR = root.right;
        BinaryNode TempL = root.left;

        root.right = TempL;
        root.left = TempR;

        invertTree(TempR);
        invertTree(TempL);

        return root;
    }

    //判断是否是平衡树
    public boolean isBalanced(BinaryNode root) {
        //递归结束条件
        if (root == null) return true;
        //普通结点的判断
        int LHeight = getHeight(root.left);
        int RHeight = getHeight(root.right);

        return Math.abs(LHeight - RHeight) < 2 && isBalanced(root.left) && isBalanced(root.right);
    }

    //判断是否是平衡树
    public boolean isBalanced2(BinaryNode root) {
        //递归结束条件
        if (root == null) return true;
        //普通结点的判断
        int LHeight = getHeight(root.left);
        int RHeight = getHeight(root.right);

        if (Math.abs(LHeight - RHeight) > 1) //否定的逻辑则可提前判断
            return false;
        return isBalanced2(root.left) && isBalanced2(root.right);
    }

    //判断是否是平衡树的高效解法 O(n)
    public boolean isBalanced3(BinaryNode root) {
        //递归结束条件
        if (root == null) return true;
        //普通结点的判断
        int LHeight = getHForisBalanced(root.left);
        int RHeight = getHForisBalanced(root.right);

        return getHForisBalanced(root) >= 0;
    }

    public static int getHForisBalanced(BinaryNode root) {
        if (root == null) return 0;
        int L = getHForisBalanced(root.left);
        int R = getHForisBalanced(root.right);

        if (L >= 0 && R >= 0 && Math.abs(L - R) < 2) return 1 + Math.max(L, R);
        else return -1;
    }

    //对称二叉树
    public boolean isSymmetric(BinaryNode root) {
        if (root == null) return true;
        return isSymmetricChild(root.left, root.right);
    }

    public static boolean isSymmetricChild(BinaryNode root1, BinaryNode root2) {
        if (root1 == null && root2 != null || root1 != null && root2 == null) return false;
        if (root1 == null && root2 == null) return true; //由于对于任何结点来说,为空一定是相同的,而且也是为排除空指针异常问题
        if (root1.val != root2.val) return false;
        return isSymmetricChild(root1.left, root2.right) && isSymmetricChild(root1.right, root2.left);
    }

    //先序遍历字符串创建二叉树 https://www.nowcoder.com/practice/4b91205483694f449f94c179883c1fef?tpId=60&&tqId=29483&rp=1&ru=/activity/oj&qru=/ta/tsing-kaoyan/question-ranking
    static int sub = 0;

    public static BinaryNode CreateTreeByPre(BinaryNode root, String exp) {
        if (sub >= exp.length()) return null; //可以不要,因为二叉树的结构会自动结束(sub最好还是定义在外面,因为使用值传递会比较麻烦,返回到某个结点时,值并不会变)
        char temp = exp.charAt(sub);
        sub++;
        if (temp == '#') {
            return null;
        }
        BinaryNode node = new BinaryNode(temp);
        node.left = CreateTreeByPre(node, exp);
        node.right = CreateTreeByPre(node, exp);
        return node;
    }

    //层序遍历
    public static List<List<Character>> levelOrder(BinaryNode root) {
        Queue<BinaryNode> queue1 = new LinkedList<>();
        Queue<BinaryNode> queue2 = new LinkedList<>();
        Queue[] queues = {queue1, queue2};//用于存储元素

        List<List<Character>> Ret = new ArrayList<>();//返回用
        //为什么不能使用 2 的 N 次方 去判断在哪层呢?--因为有空结点的情况
        int change = 0; //变换队列
        if (root != null) queue1.offer(root);
        else return null; //可以不要,因为Ret默认为空
        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            List<Character> list = new ArrayList<>();
            while (!queues[change].isEmpty()) {
                BinaryNode temp = (BinaryNode) queues[change].poll();
                //System.out.print(temp.val + " ");
                //处理返回值
                list.add(temp.val);
                //处理子树
                if (temp.left != null) //避免null指针进入
                    queues[(change + 1) % 2].offer(temp.left);
                if (temp.right != null) //避免null指针进入
                    queues[(change + 1) % 2].offer(temp.right);
            }
            change = (change + 1) % 2; //一次使用一个队列,如果在0循环的位置就改为1了,那就继续判断为同一层了,会造成数据丢失(还没出完一个队列就换了)
            Ret.add(list);
        }
        return Ret;
    }

    // 判断一棵树是不是完全二叉树
    public static boolean isCompleteTree(BinaryNode root) {
        return true;
    }

    //通过 前序遍历 与 中序遍历 构造二叉树
    public BinaryNode buildTree1(int[] preorder, int[] inorder) {
        return null;
    }

    //通过 后序遍历 与 中序遍历 构造二叉树
    public BinaryNode buildTree2(int[] inorder, int[] postorder) {
        return null;
    }

    //二叉树的最近公共祖先
    public BinaryNode lowestCommonAncestor(BinaryNode root, BinaryNode p, BinaryNode q) {
        return null;
    }

    //二叉树前序非递归遍历实现
    public static void preOrderNor(BinaryNode root) {

    }

    //二叉树中序非递归遍历实现
    public static void inOrderNor(BinaryNode root) {

    }

    //二叉树后序非递归遍历实现
    public static void postOrderNor(BinaryNode root) {

    }

    //根据二叉树创建字符串
    public String tree2str(BinaryNode root) {
        return null;
    }

    //这是一个 ,使用特殊情况帮我测试一下,看看是否会出错
    public static void main(String[] args) {
        BinaryNode root = CreateTree();
        levelOrder(root);
    }
}
