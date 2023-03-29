import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/29
 * Time: 21:47
 */


class SingleNode {
    int data;//数据
    SingleNode next;//指针

    public SingleNode(int x) {
        this.data = x;
    }
}

public class SingleLinkList {
    SingleNode phead;//链表的头

    public void addFirst(int data) //头插法
    {
        SingleNode pos = phead;
        SingleNode temp = new SingleNode(data);
        temp.next = pos;
        phead = temp;
    }

    public void addLast(int data) //尾插法
    {
        SingleNode pos = phead;
        if (pos == null) {
            addFirst(data);
        } else {
            while (pos.next != null) {
                pos = pos.next;
            }
            pos.next = new SingleNode(data);
        }
    }

    public boolean addIndex(int index, int data) //任意位置插入,第一个数据节点为0号下标
    {
        SingleNode pd = phead;
        SingleNode pos = phead;
        int size = size();
        if (index > size || index < 0) {
            System.out.printf("非法位置!请重新输入位置:");
            Scanner sc = new Scanner(System.in);
            int new_index = sc.nextInt();
            System.out.println();
            addIndex(new_index, data);
        } else if (index == size) {
            addLast(data);
        } else if (index == 0) {
            addFirst(data);
        } else {
            SingleNode prev = null;
            //寻址,pos位置即为index所指向位置
            for (int i = 0; i < index; i++) {
                prev = pos;
                pos = pos.next;
            }
            SingleNode temp = new SingleNode(data);
            temp.next = prev.next;
            prev.next = temp;
        }
        return true;
    }

    public boolean contains(int key) //查找是否包含关键字key是否在单链表当中
    {
        SingleNode pos = phead;
        while (pos != null) {
            if (pos.data == key) {
                System.out.println("存在该数:" + key);
                return true;
            }
            pos = pos.next;
        }
        System.out.println("不存在该数:" + key);
        return false;
    }

    public void remove(int key) //删除第一次出现关键字为key的节点
    {
        SingleNode pos = phead;
        SingleNode prev = null;
        while (pos != null) {
            if (pos.data == key) {
                if (prev == null) {
                    phead = pos.next;
                    System.out.println("已删除该数据:" + key);
                    return;
                }
                prev.next = pos.next;
                System.out.println("已删除该数据:" + key);
                return;
            }
            prev = pos;
            pos = pos.next;
        }
        System.out.println("不存在该数据,无法删除!");
        return;
    }

    public void removeAllKey(int key)  //删除所有值为key的节点
    {
        SingleNode pos = phead;
        SingleNode prev = null;
        int flag = 0;
        while (pos != null) {
            if (pos.data == key) {
                if (prev == null) //头就是该数值时
                {
                    phead = pos.next;
                    pos = pos.next;
                    flag = 1;
                    continue;
                }
                prev.next = pos.next;
                pos = pos.next;
                flag = 1;
                continue;
            }
            prev = pos;
            pos = pos.next;
        }
        if (flag == 1) {
            System.out.println("该元素:" + key + " 已经全部删除");
            return;
        }
        if (flag == 0) {
            System.out.println("不存在该数据,无法删除!");
            return;
        }
    }

    public int size() //得到单链表的长度
    {
        SingleNode pos = phead;
        int size = 0;
        while (pos != null) {
            pos = pos.next;
            size++;
        }
        return size;
    }

    public void display() //打印全部数据
    {
        SingleNode pd = phead;
        SingleNode pos = phead;
        System.out.printf("链表数据:");
        while (pos != null) {
            System.out.printf("%d  ", pos.data);
            pos = pos.next;
        }
        System.out.println();
    }

    //独立的打印
    public static void display(SingleNode phead) //打印全部数据
    {
        SingleNode pd = phead;
        SingleNode pos = phead;
        System.out.printf("链表数据:");
        while (pos != null) {
            System.out.printf("%d  ", pos.data);
            pos = pos.next;
        }
        System.out.println();
    }

    public static void displayOnly(SingleNode pos) //打印当前节点的数据
    {
        if (pos == null) {
            System.out.println("空节点!");
            return;
        }
        System.out.printf("该节点的数据为:");
        System.out.printf("%d ", pos.data);
        System.out.println();
    }

    public void clear() //清空链表
    {
        phead = null;
    }

    public void reverseList() //翻转链表
    {
        if (phead == null) {
            System.out.println("错误:空链表");
            return;
        }
        SingleNode prev = null;
        SingleNode cur = phead;
        while (cur != null) {

            SingleNode next = cur.next;
            cur.next = prev; //由于prev初始化为NULL,正好解决了头结点逆置后需要将this.next置空
            prev = cur;
            cur = next;
        }
        phead = prev;
    }


    public SingleNode middleNode()  //返回中间节点
    {
        SingleNode slow = phead;
        SingleNode fast = phead;
        if (phead == null) {
            return null;
        }
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    public SingleNode FindKthToTail(int k) //输入一个链表，输出该链表中倒数第k个结点。
    {
        SingleNode slow = phead;
        SingleNode fast = phead;
        if (phead == null) {
            return null;
        }
        for (int i = 0; i < k; i++) {
            fast = fast.next;
            if (fast == null && i < k - 1) //为判断先走K了,但是恰好为null,这样就可以直接返回slow即可
                return null;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    public static SingleNode mergeTwoLists(SingleNode list1, SingleNode list2) {
        SingleNode pos1 = list1;
        SingleNode pos1_prev = null;
        SingleNode pos2 = list2;
        SingleNode pos2_prev = null;
        SingleNode temphead = new SingleNode(-1);
        SingleNode index = temphead;
        SingleNode newhead = null;
        if (pos1 == null && pos2 == null) {
            return null;
        } else if (pos1 == null) {
            return pos2;
        } else if (pos2 == null) {
            return pos1;
        }
        int flag = 0;
        while (pos1 != null && pos2 != null) //之前已经判断完了开始就是空的情况,现在是每次循坏后产生的
        {
            if (pos2.data > pos1.data) {
                index.next = pos1;
                index = index.next;
                pos1_prev = pos1;
                pos1 = pos1.next;
                pos1_prev.next = null;
            } else {
                index.next = pos2;
                index = index.next;
                pos2_prev = pos2;
                pos2 = pos2.next;
                pos2_prev.next = null;
            }
            if (flag == 0) {
                newhead = index;
                flag = 1;
            }

        }
        if (pos1 == null && pos2 == null) {
            return newhead;
        } else if (pos1 == null) {
            index.next = pos2;
        } else {
            index.next = pos1;
        }
        return newhead;
    }

    public SingleNode partition(int x) //链表分割
    {
        SingleNode pos = phead;
        if (pos == null) {
            return null;
        }
        SingleNode newheadS = new SingleNode(-1);
        SingleNode S_pos = newheadS;
        SingleNode newheadB = new SingleNode(-1);
        SingleNode B_pos = newheadB;
        while (pos != null) {
            if (pos.data > x) {
                B_pos.next = pos;
                B_pos = B_pos.next;
            } else {
                S_pos.next = pos;
                S_pos = S_pos.next;
            }
            pos = pos.next;
        }
        S_pos.next = newheadB.next;
        B_pos.next = null;
        return newheadS.next;
    }

    //内部测试接口
    public static void main(String[] args) {
        SingleLinkList ls1 = new SingleLinkList();
        ls1.addFirst(30);
        ls1.addLast(20);
        ls1.addLast(30);
        ls1.addLast(40);
        ls1.addLast(5);
        ls1.display();
        ls1.display(ls1.partition(10));

        ls1.clear();
    }
}
