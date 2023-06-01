#include <stdio.h>
#include <stdlib.h>

typedef struct road {
    int require;
    int already;
} Roads;

#define GO 100 // ָ���ʼ���ڴŵ���

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

// ��ʼ������ʹ�ͷλ��
void initRequire(Roads Require[], int RequireRoads[], int *init) {
    for (int i = 0; i < 9; i++) {
        Require[i].require = RequireRoads[i];
        Require[i].already = 0; // ��־δ������
    }
    *init = GO;
}

void menu() {
    printf("0.�˳�\n");
    printf("1.FCFS�㷨\n");
    printf("2.SSTF�㷨\n");
    printf("3.SCAN�㷨\n");
    printf("4.CSCAN�㷨\n");
    printf("5.��ʾ�Աȱ�\n");
    printf("������:");
}

void FCFS(Roads Require[], int *init, int AllRoads[], float Average[]) {
    int CurrentRoad = 0;
    printf("����˳��\n");
    for (int i = 0; i < 9; i++) {
        CurrentRoad = abs(Require[i].require - *init) + CurrentRoad;
        *init = Require[i].require;
        printf("%d ", Require[i].require);
    }
    printf("\n");
    AllRoads[0] = CurrentRoad;
    Average[0] = CurrentRoad / 9.0;
    printf("FCFS�㷨ִ����� ��Ѱ������%d ƽ��Ѱ������%0.1f\n", AllRoads[0], Average[0]);
}

void SSTF(Roads Require[], int *init, int AllRoads[], float Average[]) {
    int miniRoad = 0, currentRoad = 0, k = 0;
    printf("����˳��\n");
    // K������¼�Ƿ��ǵ�һ�η��ʵ�δ���Ĵŵ�
    for (int i = 0; i < 9; i++) {
        // Ѱ���뵱ǰ��ͷ���λ��
        for (int i = 0; i < 9; i++) {
            if (k == 0 && Require[i].already == 0) {
                // �ҵ�δ���ʵ�һ��ʱ
                miniRoad = abs(Require[i].require - *init);
                k++;
                continue;
            } else if (Require[i].already == 0 && miniRoad > abs(Require[i].require - *init)) {
                miniRoad = abs(Require[i].require - *init);
            }
        }
// ����Ѿ�����ȡ��
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
    printf("SSTF�㷨ִ����� ��Ѱ������%d ƽ��Ѱ������%0.1f\n", AllRoads[1], Average[1]);
}

void SCAN(Roads Require[], int *init, int AllRoads[], float Average[]) {
    int CurrentRoad = 0, direction = 1, flag = 0, k = 0;
    printf("����˳��\n");
    while (1) {
        flag = 0;
// Ѱ�ҵ�ǰ����������Ĵŵ�
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
// �������ɨ��ʱδ�ҵ����ı䷽���򷴷���ɨ��
        if (flag == 0 && direction == 1) {
            direction = -1;
            continue;
        }
// �������ɨ��ʱδ�ҵ�������ѭ��
        else if (flag == 0 && direction == -1) {
            break;
        }
    }
    printf("\n");
    AllRoads[2] = CurrentRoad;
    Average[2] = CurrentRoad / 9.0;
    printf("SCAN�㷨ִ����� ��Ѱ������%d ƽ��Ѱ������%0.1f\n", AllRoads[2], Average[2]);
}

void CSCAN(Roads Require[], int *init, int AllRoads[], float Average[]) {
    int CurrentRoad = 0, direction = 1, flag = 0, k = 0;
    printf("����˳��\n");
    while (1) {
        flag = 0;
// Ѱ�ҵ�ǰ����������Ĵŵ�
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
// �������ɨ��ʱδ�ҵ�������С�Ĵŵ���ʼ������ɨ��
        if (flag == 0 && direction == 1) {
            direction = -1;
            *init = 0;
            continue;
        }
// �������ɨ��ʱδ�ҵ��������Ĵŵ���ʼ���ı䷽����������ɨ��
        else if (flag == 0 && direction == -1) {
            direction = 1;
            *init = 199;
            flag = 1;
        }
// ����ҵ�������δ���ŵ�������ѭ��
        if (k == 9) {
            break;
        }
    }
    printf("\n");
    AllRoads[3] = CurrentRoad;
    Average[3] = CurrentRoad / 9.0;
    printf("CSCAN�㷨ִ����� ��Ѱ������%d ƽ��Ѱ������%0.1f\n", AllRoads[3], Average[3]);
}

void PrintResult(int AllRoads[], float Average[]) {
    printf("�㷨    ��Ѱ����    ƽ��Ѱ����\n");
    printf("------------------------------------\n");
    printf("FCFS\t%d\t%0.1f\n", AllRoads[0], Average[0]);
    printf("SSTF\t%d\t%0.1f\n", AllRoads[1], Average[1]);
    printf("SCAN\t%d\t%0.1f\n", AllRoads[2], Average[2]);
    printf("CSCAN\t%d\t%0.1f\n", AllRoads[3], Average[3]);
}