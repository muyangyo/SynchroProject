#include <stdio.h>
#include <conio.h>

// 信号量类型定义为 int 类型
typedef int semaphore;

// 进程结构体，暂时为空
struct process {
};

// 缓冲区结构体
typedef struct {
    // 资源信号量，表示可以存放数据的缓冲区数量
    int resource_count;
    // 指向进程的指针
    struct process *process_ptr;
} buffer;

// P操作：申请资源，n是资源信号量指针
void acquire_resource(int *n) {
    (*n)--;   // 申请一个资源
    if ((*n) < 0) {
        printf("没有可用资源，请等待...\n");
    } else {
        printf("成功申请到一个资源，当前可用资源数量: %d\n", (*n));
    }
}

// V操作：释放资源，n是资源信号量指针
void release_resource(int *n) {
    (*n)++;   // 释放一个资源

    if ((*n) <= 0) {
        printf("还有其他进程被阻塞，正在唤醒其中一个进程...\n");
    } else {
        printf("成功释放一个资源，当前可用资源数量: %d\n", (*n));
    }
}

// 生产者函数，mutex是互斥信号量指针，full和empty是资源信号量指针
void producer(int *mutex, int *full, int *empty) {
    printf("生产者进程已启动...\n\n");
    while (1) {
        printf("正在生成一个数据项...\n");
        acquire_resource(empty);  // 获得空闲缓冲区单元 ，先获得资源
        acquire_resource(mutex);  // 再获得进入临界区的资格
        printf("成功将数据项放入缓冲区中.\n"); // 将数据放入缓冲区
        release_resource(mutex);  // 离开临界区
        release_resource(full);  // 缓冲区满
        getch();
    }
}

// 消费者函数，mutex是互斥信号量指针，full和empty是资源信号量指针
void consumer(int *mutex, int *full, int *empty) {
    printf("消费者进程已启动...\n\n");
    while (1) {
        acquire_resource(full);  // 获得满缓冲区单元 ，先获得资源
        acquire_resource(mutex);  // 再获得进入临界区的资格
        printf("成功从缓冲区取出一个数据项.\n"); // 从缓冲区取走数据
        release_resource(mutex);  // 离开临界区
        release_resource(empty);  // 缓冲区空
        printf("成功消费了数据项.\n");// 消费数据
        getch();
    }
}

int main() {
    // 定义三个信号量：互斥信号量、空闲缓冲区信号量、满缓冲区信号量
    semaphore mutex = 1;
    semaphore empty = 3;
    semaphore full = 0;

    // 调用生产者函数和消费者函数
    producer(&mutex, &full, &empty);
    consumer(&mutex, &full, &empty);

    return 0;
}
