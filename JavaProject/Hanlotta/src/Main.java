/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/21
 * Time: 16:52
 */
public class Main {

    public static void Move(char Origin, char Dest) {
        System.out.print("  " + Origin + "->" + Dest);
    }

    public static void Hannelotte(int x, char Origin, char Trans, char Dest) {

        if (x == 1) {
            //直接移动就行,不用利用中转位置
            Move(Origin, Dest);
        } else {
            //>1 就先把其他   x-1   个盘子移动到 中转位置 上,此时我们的  x-1 个盘子的目标位置应该是中转位置,而最终的目标位置此时就是中转位置
            Hannelotte(x - 1, Origin, Dest, Trans);
            //然后将第  X 个盘子移动到最终的目标位置
            Move(Origin, Dest);
            //再移动 x-1 个盘子,将最大的移动到   最终目的地
            Hannelotte(x - 1, Trans, Origin, Dest);
        }

    }

    public static void main(String[] args) {
        Hannelotte(1,'A','B','C');
        System.out.println();
        Hannelotte(2,'A','B','C');
        System.out.println();
        Hannelotte(3,'A','B','C');
        System.out.println();
    }
    //1 A->C
    //2 A->B A->C B->C
    //3 A->C A->B C->B A->C B->A B->C A->C
}
