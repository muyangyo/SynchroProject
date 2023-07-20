package ForOJ;


import BinarySearchTreepackage.BinarySearchTree;

//树中节点的左指针需要指向前驱，树中节点的右指针需要指向后继
//https://www.nowcoder.com/practice/947f6eb80d944a84850b0538bf0ec3a5?tpId=13&&tqId=11179&rp=1&ru=/activity/oj&qru=/ta/coding-interviews/question-ranking
public class Solution {

    //利用中序遍历的有序性
    public static BinarySearchTree.BinarySearchNode Convert(BinarySearchTree.BinarySearchNode root) {
        cur = new BinarySearchTree.BinarySearchNode(-1);//临时头结点
        BinarySearchTree.BinarySearchNode ret = cur;
        first = true;
        convertHelper(root);
        return ret.right;
    }

    static boolean first;
    static BinarySearchTree.BinarySearchNode cur;

    public static void convertHelper(BinarySearchTree.BinarySearchNode root) {
        //回返条件
        if (root == null) return;

        //向左走
        convertHelper(root.left);

        //处理当前结点
        if (first) {
            cur.right = root;
            cur = root;
            first = false;
        } else {
            cur.right = root;
            root.left = cur;
            cur = root;
        }

        //向右走
        convertHelper(root.right);
    }

    public static void print(BinarySearchTree.BinarySearchNode root) {
        //回返条件
        if (root == null) return;

        //向左走
        print(root.left);

        //处理当前结点
        System.out.print(root.val + " ");

        //向右走
        print(root.right);
    }


    public static void main(String[] args) {
        BinarySearchTree binarySearchTree = new BinarySearchTree();
        binarySearchTree.insert(10);
        binarySearchTree.insert(6);
        binarySearchTree.insert(14);
        binarySearchTree.insert(4);
        binarySearchTree.insert(8);
        binarySearchTree.insert(12);
        binarySearchTree.insert(16);
        BinarySearchTree.BinarySearchNode Temp = Convert(binarySearchTree.root);
    }

}
