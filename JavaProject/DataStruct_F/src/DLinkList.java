/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/4/10
 * Time: 20:19
 */
class DListNode<T extends Comparable> {
    DListNode<Comparable> prev;
    T val;
    DListNode<Comparable> next;

    public DListNode(T val) {
        this.val = val;
    }
}


//无头双向不循环链表(int)
public class DLinkList {
    DListNode head;
    DListNode tail;

    //头插法
    public void addFirst(int data) {
        DListNode temp = new DListNode(data);
        if (head == null) {
            head = temp;
            tail = temp;
        } else {
            head.prev = temp;
            temp.next = head;
            head = temp;
        }
    }

    //尾插法
    public void addLast(int data) {
        if (head == null) {
            addFirst(data);
        } else {
            DListNode temp = new DListNode(data);
            tail.next = temp;
            temp.prev = tail;
            tail = temp;
        }
    }

    //任意位置插入,第一个数据节点为0号下标
    public void addIndex(int index, int data) {
        int sz = size();
        if (index < 0 || index > sz) {
            System.out.println("超出范围!");
            return;
        }
        if (index == 0) {
            addFirst(data);
        } else if (index == sz) {
            addLast(data);
        } else {
            DListNode temp = new DListNode<>(data);
            DListNode cur = head;
            while (index > 0) {
                cur = cur.next;
                index--;
            }
            temp.next = cur;
            temp.prev = cur.prev;
            cur.prev.next = temp;
            cur.prev = temp;
        }
    }

    //查找是否包含关键字key是否在单链表当中
    public boolean contains(int key) {
        DListNode temp = head;
        if (temp == null)
            return false;

        while (temp != null) {
            if (temp.val.compareTo(key) == 0)
                return true;
            temp = temp.next;
        }
        return false;
    }

    //删除第一次出现关键字为key的节点
    public void remove(int key) {

    }

    //头删
    public void removeFirst() {

    }

    //尾删
    public void removeLast() {

    }
    

    //删除所有值为key的节点
    public void removeAllKey(int key) {
    }

    //得到单链表的长度
    public int size() {
        DListNode temp = head;
        int count = 0;
        while (temp != null) {
            temp = temp.next;
            count++;
        }
        return count;
    }

    public void display() {
        DListNode temp = head;
        if (temp == null) {
            System.out.println("空链表");
            return;
        }

        System.out.print("双链表数据为: | ");
        while (temp != null) {
            System.out.print(temp.val + " | ");
            temp = temp.next;
        }
    }

    public void clear() {
        head = null;
        tail = null;
    }

    public static void main(String[] args) {
        DLinkList list = new DLinkList();
        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        list.addLast(5);
        list.addIndex(2, 23);
        list.display();
        list.clear();
    }
}
