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
        private int key;
        private int value;
        private Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public int usedSize; //元素个数
    private static final float DEFAULT_LOAD_FACTOR = 0.75f; //默认的负载因子为0.75

    public HashBucket() {
        this.array = new Node[10];
    }

    public void put(int key, int val) {
        // code here
    }

    private void resize() {
        // code here
    }

    private float loadFactor() {
        // code here
    }

    public int get(int key) {
        // code here
    }
}
}
