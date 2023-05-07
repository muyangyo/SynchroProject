package ForOJ;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/5/6
 * Time: 22:15
 */
public class SolutionMethod {

    //通过 前序遍历 与 中序遍历 构造二叉树
    public static TreeNode buildTree1(int[] preorder, int[] inorder) {
        TreeNode Ret = Helper_Pre_In(inorder, 0, inorder.length - 1, preorder);
        PrePos = 0;//重置为0,便于下次调用
        return Ret;
    }

    /**
     * 中 (根本依据)
     *
     * @param inorder 当前结点开始的中序遍历
     * @param InStart 当前结点的中序遍历 头下标
     * @param InEnd 当前结点的中序遍历 尾下标(包含)
     * ------------------------------------------------
     * 前
     * @param preorder 总前序遍历
     * PrePos 总前序遍历下标
     */

    static int PrePos = 0; //由于二叉树的递归的变量有 停留性 ,我们要保持一个变量一直在变,那就要在外面定义

    public static TreeNode Helper_Pre_In(int[] inorder, int InStart, int InEnd, int[] preorder) {
        if (inorder == null || preorder == null || inorder.length != preorder.length) return null; //排除空数组造成的空指针异常
        //终止条件(回返条件)
        if (InStart > InEnd || InStart >= inorder.length || InEnd < 0) return null;//正好置空
        TreeNode root = new TreeNode(preorder[PrePos]);//处理当前结点本身
        PrePos++;

        int InRootPos = Search(inorder, root.val);
        root.left = Helper_Pre_In(inorder, InStart, InRootPos - 1, preorder);//将左边的所有结点看成一个结点,即为左树
        root.right = Helper_Pre_In(inorder, InRootPos + 1, InEnd, preorder);//将右边的所有结点看成一个结点,即为右树

        return root;
    }

    public static int Search(int[] inorder, int key) {
        for (int i = 0; i < inorder.length; i++) {
            if (inorder[i] == key) return i;
        }
        return -1;//没找到
    }


    //通过 后序遍历 与 中序遍历 构造二叉树
    public static TreeNode buildTree2(int[] inorder, int[] postorder) {
        PostPos = postorder.length - 1;//初始化和重置效果
        TreeNode Ret = Helper_Post_In(inorder, 0, inorder.length - 1, postorder);
        return Ret;
    }

    static int PostPos = 0;

    public static TreeNode Helper_Post_In(int[] inorder, int InStart, int InEnd, int[] postorder) {
        if (inorder == null || postorder == null || inorder.length != postorder.length) return null; //排除空数组造成的空指针异常
        //终止条件(回返条件)
        if (InStart > InEnd || InStart >= inorder.length || InEnd < 0) return null;//正好置空
        TreeNode root = new TreeNode(postorder[PostPos]);//处理当前结点本身
        PostPos--;

        int InRootPos = Search(inorder, root.val);

        //注意: 要先构建右树再构建左树
        root.right = Helper_Post_In(inorder, InRootPos + 1, InEnd, postorder);//将右边的所有结点看成一个结点,即为右树
        root.left = Helper_Post_In(inorder, InStart, InRootPos - 1, postorder);//将左边的所有结点看成一个结点,即为左树


        return root;
    }


}
