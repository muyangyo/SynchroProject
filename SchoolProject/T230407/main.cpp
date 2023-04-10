#include <stdio.h>

#define N 50   // 定义最大进程数

struct PCB {
    int pn;   // 进程名字
    int at;   // 到达时间
    int st;   // 服务时间
    int ct;   // 完成时刻
    int sc;   // 标志是否完成
    int st1;  // 剩余服务时间
} process[N];  // 创建 PCB 结构体类型数组 process

void sjp(int n) {

    int i, j, T;
    printf("\n请输入时间片：\n");
    scanf("%d", &T);
    for (i = 1; i <= n; i++) {
        process[i].sc = 0;  // 初始化所有进程的状态为未完成
        printf("\n%d:\n请依次输入进程的信息\n请输入进程名字:", i);
        scanf("%d", &process[i].pn);    // 输入进程名字
        printf("请输入到达时间:");
        scanf("%d", &process[i].at);    // 输入到达时间
        printf("请输入服务时间:");
        scanf("%d", &process[i].st);    // 输入服务时间
        process[i].st1 = process[i].st; // 初始化剩余服务时间为服务时间
    }

    // 按照各进程到达时间升序，对进程排序
    for (i = 1; i <= n; i++)
        for (j = i + 1; j <= n; j++)
            if (process[j].at < process[i].at) {
                // 交换进程 i 和进程 j 的位置
                process[0] = process[j];
                process[j] = process[i];
                process[i] = process[0];
            }

    int time = process[1].at;   // 初始化时间为第一个进程到达的时间
    int flag = 1;
    int sum = 0;
    printf("\n   调度进程次数 运行的进程 开始运行时间 运行时间 剩余服务时间 结束时间\n");
    int z = 1;

    while (sum < n) {
        flag = 0;
        for (i = 1; i <= n; i++) {
            if (process[i].sc == 1) continue;   // 如果进程已经完成，则跳过
            else {
                if (process[i].st1 <= T && time >= process[i].at) {   // 判断是否能够一次性运行完该进程
                    flag = 1;
                    time = time + process[i].st1;   // 更新时间
                    process[i].sc = 1;   // 标志该进程已完成
                    process[i].ct = time;   // 记录该进程完成时刻
                    printf("Time:%-10d%-10d%-10d%-10d%-10d%-10d\n", z++, process[i].pn, time - process[i].st1,
                           process[i].st1, 0, time);    // 输出进程调度信息
                    process[i].st1 = 0;   // 更新剩余服务时间为 0
                } else if (process[i].st1 > T && time >= process[i].at) {   // 判断是否需要分多次运行
                    flag = 1;
                    time = time + T;    // 更新时间
                    process[i].st1 -= T;   // 更新剩余服务时间
                    printf("Time:%-10d%-10d%-10d%-10d%-10d%-10d\n", z++, process[i].pn, time - T, T, process[i].st1,
                           time);   // 输出进程调度信息
                }
                if (process[i].sc == 1) sum++;   // 如果该进程已完成，则累加已完成的进程数量
            }
        }

        if (flag == 0 && sum < n) {
            for (i = 1; i <= n; i++)
                if (process[i].sc == 0) {
                    time = process[i].at;   // 如果当前没有进程可以调度，切换到下一个未完成的进程
                    break;
                }
        }

    }
}

int main() {
    int n;
    printf("\t\t时间片轮转调度算法\n");
    printf("请输入总进程数：\n");
    scanf("%d", &n);
    sjp(n);
    return 0;
}
