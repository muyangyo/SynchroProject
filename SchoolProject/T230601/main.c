#include <stdio.h>
#include <stdlib.h>

typedef struct road {
    int require;
    int already;
} Roads;

#define GO 100 // 指针初始所在磁道数

void menu();

void FCFS(Roads Require[], int *init, int AllRoads[], float Average[]);

void SCAN(Roads Require[], int *init, int AllRoads[], float Average[]);

void SSTF(Roads Require[], int *init, int AllRoads[], float Average[]);

void CSCAN(Roads Require[], int *init, int AllRoads[], float Average[]);

void PrintResult(int AllRoads[], float Average[]);

void initRequire(Roads Require[], int RequireRoads[], int *init);

int main() {
    int init = GO;
    int RequireRoads[] = {55, 58, 39, 18, 90, 160, 150, 38, 184};
    Roads Require[9];
    int AllRoads[4];
    float Average[4];

    initRequire(Require, RequireRoads, &init);
    while (1) {
        menu();
        char user;
        scanf("%c", &user, sizeof(user));
        getchar();
        switch (user) {
            case '0':
                return 0;
            case '1':
                FCFS(Require, &init, AllRoads, Average);
                initRequire(Require, RequireRoads, &init);
                break;
            case '2':
                SSTF(Require, &init, AllRoads, Average);
                initRequire(Require, RequireRoads, &init);
                break;
            case '3':
                SCAN(Require, &init, AllRoads, Average);
                initRequire(Require, RequireRoads, &init);
                break;
            case '4':
                CSCAN(Require, &init, AllRoads, Average);
                initRequire(Require, RequireRoads, &init);
                break;
            case '5':
                PrintResult(AllRoads, Average);
                break;
        }
    }
}

// 初始化数组和磁头位置
void initRequire(Roads Require[], int RequireRoads[], int *init) {
    for (int i = 0; i < 9; i++) {
        Require[i].require = RequireRoads[i];
        Require[i].already = 0; // 标志未被访问
    }
    *init = GO;
}

void menu() {
    printf("0.退出\n");
    printf("1.FCFS算法\n");
    printf("2.SSTF算法\n");
    printf("3.SCAN算法\n");
    printf("4.CSCAN算法\n");
    printf("5.显示对比表\n");
    printf("请输入:");
}

void FCFS(Roads Require[], int *init, int AllRoads[], float Average[]) {
    int CurrentRoad = 0;
    printf("服务顺序：\n");
    for (int i = 0; i < 9; i++) {
        CurrentRoad = abs(Require[i].require - *init) + CurrentRoad;
        *init = Require[i].require;
        printf("%d ", Require[i].require);
    }
    printf("\n");
    AllRoads[0] = CurrentRoad;
    Average[0] = CurrentRoad / 9.0;
    printf("FCFS算法执行完毕 总寻道数：%d 平均寻道数：%0.1f\n", AllRoads[0], Average[0]);
}

void SSTF(Roads Require[], int *init, int AllRoads[], float Average[]) {
    int miniRoad = 0, currentRoad = 0, k = 0;
    printf("服务顺序：\n");
    // K用来记录是否是第一次访问到未读的磁道
    for (int i = 0; i < 9; i++) {
        // 寻找离当前磁头最短位置
        for (int i = 0; i < 9; i++) {
            if (k == 0 && Require[i].already == 0) {
                // 找到未访问第一个时
                miniRoad = abs(Require[i].require - *init);
                k++;
                continue;
            } else if (Require[i].already == 0 && miniRoad > abs(Require[i].require - *init)) {
                miniRoad = abs(Require[i].require - *init);
            }
        }
// 标记已经被读取过
        for (int i = 0; i < 9; i++) {
            if (miniRoad == abs(Require[i].require - *init) && Require[i].already == 0) {
                currentRoad += miniRoad;
                *init = Require[i].require;
                Require[i].already = 1;
                printf("%d ", *init);
                break;
            }
        }
    }
    printf("\n");
    AllRoads[1] = currentRoad;
    Average[1] = currentRoad / 9.0;
    printf("SSTF算法执行完毕 总寻道数：%d 平均寻道数：%0.1f\n", AllRoads[1], Average[1]);
}

void SCAN(Roads Require[], int *init, int AllRoads[], float Average[]) {
    int CurrentRoad = 0, direction = 1, flag = 0, k = 0;
    printf("服务顺序：\n");
    while (1) {
        flag = 0;
// 寻找当前方向下最近的磁道
        for (int i = 0; i < 9; i++) {
            if (Require[i].already == 0 && (*init - Require[i].require) * direction >= 0) {
                flag = 1;
                CurrentRoad += abs(*init - Require[i].require);
                printf("%d ", Require[i].require);
                Require[i].already = 1;
                *init = Require[i].require;
                k++;
            }
        }
// 如果正向扫描时未找到，改变方向向反方向扫描
        if (flag == 0 && direction == 1) {
            direction = -1;
            continue;
        }
// 如果反向扫描时未找到，结束循环
        else if (flag == 0 && direction == -1) {
            break;
        }
    }
    printf("\n");
    AllRoads[2] = CurrentRoad;
    Average[2] = CurrentRoad / 9.0;
    printf("SCAN算法执行完毕 总寻道数：%d 平均寻道数：%0.1f\n", AllRoads[2], Average[2]);
}

void CSCAN(Roads Require[], int *init, int AllRoads[], float Average[]) {
    int CurrentRoad = 0, direction = 1, flag = 0, k = 0;
    printf("服务顺序：\n");
    while (1) {
        flag = 0;
// 寻找当前方向下最近的磁道
        for (int i = 0; i < 9; i++) {
            if (Require[i].already == 0 && (*init - Require[i].require) * direction >= 0) {
                flag = 1;
                CurrentRoad += abs(*init - Require[i].require);
                printf("%d ", Require[i].require);
                Require[i].already = 1;
                *init = Require[i].require;
                k++;
            }
        }
// 如果正向扫描时未找到，从最小的磁道开始，继续扫描
        if (flag == 0 && direction == 1) {
            direction = -1;
            *init = 0;
            continue;
        }
// 如果反向扫描时未找到，从最大的磁道开始，改变方向向正方向扫描
        else if (flag == 0 && direction == -1) {
            direction = 1;
            *init = 199;
            flag = 1;
        }
// 如果找到了所有未读磁道，结束循环
        if (k == 9) {
            break;
        }
    }
    printf("\n");
    AllRoads[3] = CurrentRoad;
    Average[3] = CurrentRoad / 9.0;
    printf("CSCAN算法执行完毕 总寻道数：%d 平均寻道数：%0.1f\n", AllRoads[3], Average[3]);
}

void PrintResult(int AllRoads[], float Average[]) {
    printf("算法    总寻道数    平均寻道数\n");
    printf("------------------------------------\n");
    printf("FCFS\t%d\t%0.1f\n", AllRoads[0], Average[0]);
    printf("SSTF\t%d\t%0.1f\n", AllRoads[1], Average[1]);
    printf("SCAN\t%d\t%0.1f\n", AllRoads[2], Average[2]);
    printf("CSCAN\t%d\t%0.1f\n", AllRoads[3], Average[3]);
}