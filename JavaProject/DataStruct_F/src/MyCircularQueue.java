class MyCircularQueue {
    int Capacity; //容量
    int UsedSize = 0; //已经使用了的大小
    int[] arr;
    int head;//头
    int tail;//尾

    public MyCircularQueue(int k) //构造器，设置队列长度为 k
    {
        arr = new int[k];
        Capacity = k;
        head = 0;
        tail = 0;
    }

    public boolean enQueue(int value) //向循环队列插入一个元素,如果成功插入则返回真
    {
        if (isFull()) return false;

        arr[tail] = value;
        UsedSize++;
        tail = (tail + 1) % Capacity;
        return true;
    }

    public boolean deQueue() //从循环队列中删除一个元素,如果成功删除则返回真
    {
        if (isEmpty()) return false;

        arr[head] = -1;
        UsedSize--;
        head = (head + 1) % Capacity;
        return true;
    }

    public int Front() //从队首获取元素,如果队列为空，返回 -1
    {
        if (isEmpty()) return -1;

        return arr[head];
    }

    public int Rear() //获取队尾元素,如果队列为空，返回 -1
    {
        if (isEmpty()) return -1;

        int index = tail - 1 >= 0 ? tail - 1 : Capacity - 1;//解决 0-1=-1 的情况
        return arr[index];
    }

    public boolean isEmpty() //检查循环队列是否为空
    {
        return UsedSize == 0;
    }

    public boolean isFull() //检查循环队列是否已满
    {
        return UsedSize == Capacity;
    }
}

