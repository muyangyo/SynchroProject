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
        if (temp == null) return false;

        while (temp != null) {
            if (temp.val.compareTo(key) == 0) return true;
            temp = temp.next;
        }
        return false;
    }

    //删除第一次出现关键字为key的节点
    public void remove(int key) {
        if (head == null) {
            System.out.println("空指针!");
            return;
        }
        DListNode cur = head;
        while (cur != null) {
            if (cur.val.compareTo(key) == 0) {
                if (cur == head && head == tail) {
                    clear();
                    return;
                } else if (cur == head) {
                    removeFirst();
                    return;
                } else if (cur == tail) {
                    removeLast();
                    return;
                } else {
                    cur.next.prev = cur.prev;
                    cur.prev.next = cur.next;
                    return;
                }
            }
            cur = cur.next;
        }
    }

    //头删
    private void removeFirst() {
        if (head == null) {
            System.out.println("空指针!");
            return;
        }
        head = head.next;
        head.prev = null;
    }

    //尾删
    private void removeLast() {
        if (tail == null) {
            System.out.println("空指针");
            return;
        }
        tail.prev.next = null;
        tail = tail.prev;
    }


    //删除所有值为key的节点
    public void removeAllKey(int key) {
        if (head == null) {
            System.out.println("空指针!");
            return;
        }
        DListNode cur = head;
        while (cur != null) {
            if (cur.val.compareTo(key) == 0) {
                if (cur == head && head == tail) {
                    clear();
                } else if (cur == head) {
                    removeFirst();
                } else if (cur == tail) {
                    removeLast();
                } else {
                    cur.next.prev = cur.prev;
                    cur.prev.next = cur.next;
                }
            }
            cur = cur.next;
        }
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
        System.out.println();
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
        list.addLast(2);
        list.addLast(2);
        list.addLast(5);
        list.display();

        list.remove(1);
        list.display();

        list.remove(3);
        list.display();

        list.remove(5);
        list.display();

        list.removeAllKey(2);
        list.display();

        list.clear();
    }
}
