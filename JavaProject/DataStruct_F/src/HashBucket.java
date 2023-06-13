/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/6/12
 * Time: 9:04
 */
public class HashBucket {
    public Node[] array;//hash数组

    //数组挂载结点
    static class Node {
        private Integer key;
        private int value;
        private Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public int usedSize; //元素个数(不是链表的个数,是全部元素)
    private static final float DEFAULT_LOAD_FACTOR = 0.75f; //默认的负载因子为0.75

    public HashBucket() {
        this.array = new Node[10];//初始化为 10 个桶
    }

    public void put(int key, int val) {
        Node tempNode = new Node(key, val);
        int hash = tempNode.key.hashCode();//生成 hash值
        int index = getIndex(hash);//获取下标

        //开始寻找
        Node cur = array[index];
        while (cur != null) {
            if (cur.key.equals(tempNode.key)) {
                System.out.println("已存在该元素,仅更新 val 值");
                cur.value = val;
                return;//更新完退出即可
            }
            cur = cur.next;//向后走
        }

        // cur 为 null 了,即没有该元素
        //采用链表头插法(更简单点)
        tempNode.next = array[index];
        array[index] = tempNode;
        usedSize++;

        //判断负载因子是否超过 0.75 ,超过则扩容,但是扩容后会导致 Index 改变,则全部需要重新哈希后重新放置
        if (loadFactor() >= DEFAULT_LOAD_FACTOR) {
            resize();
        }
    }


    private int getIndex(int hash) {
        return hash % array.length;
    }

    private void resize() {
        Node[] oldArr = array;
        array = new Node[oldArr.length * 2];//两倍扩容
        usedSize = 0;//重置,后面 put 时会增加

        //遍历旧数据,重新哈希
        for (int i = 0; i < oldArr.length; i++) {
            Node cur = oldArr[i];
            while (cur != null) {
                put(cur.key + 0, cur.value + 0);//重新 放入
                cur = cur.next;
            }
        }


    }

    private float loadFactor() {
        return ((float) usedSize) / array.length; // 负载因子 = 填入表中的元素个数 / 数组长度
    }

    public int getVal(Integer key) {
        int hash = key.hashCode();
        int index = getIndex(hash);

        Node cur = array[index];
        while (cur != null) {
            if (cur.key.equals(key)) {
                return cur.value;//找到回返 val
            }
            cur = cur.next;
        }

        //没找到
        return -1;
    }

}
