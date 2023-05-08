import java.util.*;

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

        @Override
        public String toString() {
            return "BinaryNode{" + val + '}';
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
        System.out.print(root.val + " ");
        inOrder(root.right);
    }

    //后序打印:左 -> 右 -> 根
    public static void postOrder(BinaryNode root) {
        if (root == null) return;
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.val + " ");
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
                //处理行值
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
        Queue<BinaryNode> queue1 = new LinkedList<>();
        Queue<BinaryNode> queue2 = new LinkedList<>();
        Queue[] queues = {queue1, queue2};//用于存储元素

        int change = 0; //变换队列
        if (root != null) queue1.offer(root);
        else return true; //空树就是一个完全二叉树

        boolean flag = true;
        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            while (!queues[change].isEmpty()) {
                BinaryNode temp = (BinaryNode) queues[change].peek();
                if (temp == null) {
                    flag = false;
                    break;
                }
                //空结点照样会进队列,只要有null就丢进去,不能放在 poll 后,因为会多弹出一个 null 结点
                temp = (BinaryNode) queues[change].poll();
                //System.out.print(temp.val + " ");
                //处理子树
                queues[(change + 1) % 2].offer(temp.left);
                queues[(change + 1) % 2].offer(temp.right);
            }
            if (!flag) break;
            change = (change + 1) % 2; //一次使用一个队列,如果在0循环的位置就改为1了,那就继续判断为同一层了,会造成数据丢失(还没出完一个队列就换了)
        }
        //出现 null 结点,直接去判还有没有不为 null 的结点(要排除第一个有值的情况),但是会出现一种特殊情况,所以必须把两个队列都出完
        change = 0;
        while (!queue1.isEmpty() || !queue2.isEmpty()) {
            if (queue1.isEmpty()) change = 1;//为空了,就直接换另一个出队,直到两个都出队完成
            BinaryNode temp = (BinaryNode) queues[change].poll();
            if (temp != null) return false;
        }
        return true;
    }

    public static void InitStack(Stack<BinaryNode> stack) {
        while (!stack.empty()) {
            stack.pop();
        }
    }

    //二叉树的最近公共祖先
    static Stack<BinaryNode> stackp = new Stack<>();
    static Stack<BinaryNode> stackq = new Stack<>();

    public static BinaryNode lowestCommonAncestor(BinaryNode root, BinaryNode p, BinaryNode q) {
        if (root == null) return null;
        InitStack(stackp);//初始化和重置stack
        InitStack(stackq);//初始化和重置stack

        //寻找路径
        SearchNodePre(root, p, stackp);
        SearchNodePre(root, q, stackq);

        //选出多出来的路径
        int sizep = stackp.size();
        int sizeq = stackq.size();
        Stack<BinaryNode> Big = sizep > sizeq ? stackp : stackq;
        int DifferenceValue = Math.abs(sizep - sizeq);
        while (DifferenceValue != 0) {
            Big.pop();
            DifferenceValue--;
        }

        while (!stackp.isEmpty() && !stackq.isEmpty()) {
            BinaryNode TP = stackp.pop();
            BinaryNode TQ = stackq.pop();
            if (TP == TQ) return TP;
        }
        return null;
    }

    public static boolean SearchNodePre(BinaryNode root, BinaryNode key, Stack<BinaryNode> stack) {
        stack.push(root);//先入栈,看看是不是冗余结点,冗余就弹出
        if (root == null) return false;
        if (root == key) return true;

        boolean ret1 = SearchNodePre(root.left, key, stack);//搜索左树
        if (ret1) return true;
        else stack.pop();//弹出多余结点

        boolean ret2 = SearchNodePre(root.right, key, stack);//搜索右树
        if (ret2) return true;
        else stack.pop();//弹出多余结点

        return false;
    }


    //二叉树前序非递归遍历实现
    //三个的核心思想都是走到左边的底
    public static void preOrderNor(BinaryNode root) {
        if (root == null) return;//空树直接返回
        BinaryNode cur = root;

        Stack<BinaryNode> stack = new Stack<>();
        stack.push(cur);
        boolean first = true;
        while (!stack.isEmpty() || cur != null) {
            while (cur != null) {
                System.out.print(cur.val + " ");
                if (!first) {
                    stack.push(cur);
                }
                first = false;
                cur = cur.left;
            }
            //cur == null 时
            cur = stack.pop().right;
        }
        System.out.println();
    }

    //二叉树中序非递归遍历实现
    public static void inOrderNor(BinaryNode root) {
        if (root == null) return;//空树直接返回
        BinaryNode cur = root;

        Stack<BinaryNode> stack = new Stack<>();
        stack.push(cur);
        boolean first = true;
        while (!stack.isEmpty() || cur != null) {
            while (cur != null) {

                if (!first) {
                    stack.push(cur);
                }
                first = false;
                cur = cur.left;
            }
            //cur == null 时
            System.out.print(stack.peek().val + " ");
            cur = stack.pop().right;
        }
        System.out.println();
    }

    //二叉树后序非递归遍历实现
    public static void postOrderNor(BinaryNode root) {
        if (root == null) return;//空树直接返回
        BinaryNode cur = root;

        Stack<BinaryNode> stack = new Stack<>();
        stack.push(cur);
        BinaryNode PreNode = null;
        boolean first = true;
        while (!stack.isEmpty() || cur != null) {
            while (cur != null) {
                if (!first) {
                    stack.push(cur);
                }
                first = false;
                cur = cur.left;
            }
            //cur == null 时
            BinaryNode temp = stack.peek();
            //右树也为 null 时,代表没有右树/已经打印过的右树
            if (temp.right == null || temp.right == PreNode) {
                temp = stack.pop();
                System.out.print(temp.val + " ");
                PreNode = temp;
            } else {
                cur = temp.right;
            }
        }
        System.out.println();
    }

    //根据二叉树创建字符串
    public String tree2str(BinaryNode root) {
        StringBuilder sb = new StringBuilder();
        tree2strHelper(root, sb);
        return sb.toString();
    }

    public static void tree2strHelper(BinaryNode root, StringBuilder sb) {
        //回返条件
        if (root == null) return;
        sb.append(root.val);  //为什么不加 "(",因为可能出现左右子树都为空的情况,这时候是不需要括号的

        if (root.left == null || root.right == null) //判断子节点是否缺失
        {
            //处理缺失情况下的根节点
            if (root.left == null && root.right == null) //两个子树都是空的情况
            {
                //不需要执行任何东西
            }
            else if (root.left == null) //左树为空时
            {
                sb.append("()(");//不用处理左树了,直接加一对括号即可
                //处理右树
                tree2strHelper(root.right, sb);
                sb.append(")");

            }
            else //右树为空时
            {
                //处理左树
                sb.append("(");
                tree2strHelper(root.left, sb);
                sb.append(")");

                //右树直接不用管了
            }
        }
        else //左右树都存在的情况,直接处理
        {
            //处理左树
            sb.append("(");
            tree2strHelper(root.left, sb);
            sb.append(")");

            //处理右树
            sb.append("(");
            tree2strHelper(root.right, sb);
            sb.append(")");

        }
    }


    //这是一个 ,使用特殊情况帮我测试一下,看看是否会出错
    public static void main(String[] args) {
        BinaryNode root = CreateTree();
        preOrderNor(root);
        preOrder(root);
        System.out.println();
        System.out.println("---------------中序打印--------------");
        inOrderNor(root);
        inOrder(root);
        System.out.println();
        System.out.println("---------------后序打印--------------");
        postOrderNor(root);
        postOrder(root);
    }
}
