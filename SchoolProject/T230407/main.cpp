#include <stdio.h>

#define N 50   // ������������

struct PCB {
    int pn;   // ��������
    int at;   // ����ʱ��
    int st;   // ����ʱ��
    int ct;   // ���ʱ��
    int sc;   // ��־�Ƿ����
    int st1;  // ʣ�����ʱ��
} process[N];  // ���� PCB �ṹ���������� process

void sjp(int n) {

    int i, j, T;
    printf("\n������ʱ��Ƭ��\n");
    scanf("%d", &T);
    for (i = 1; i <= n; i++) {
        process[i].sc = 0;  // ��ʼ�����н��̵�״̬Ϊδ���
        printf("\n%d:\n������������̵���Ϣ\n�������������:", i);
        scanf("%d", &process[i].pn);    // �����������
        printf("�����뵽��ʱ��:");
        scanf("%d", &process[i].at);    // ���뵽��ʱ��
        printf("���������ʱ��:");
        scanf("%d", &process[i].st);    // �������ʱ��
        process[i].st1 = process[i].st; // ��ʼ��ʣ�����ʱ��Ϊ����ʱ��
    }

    // ���ո����̵���ʱ�����򣬶Խ�������
    for (i = 1; i <= n; i++)
        for (j = i + 1; j <= n; j++)
            if (process[j].at < process[i].at) {
                // �������� i �ͽ��� j ��λ��
                process[0] = process[j];
                process[j] = process[i];
                process[i] = process[0];
            }

    int time = process[1].at;   // ��ʼ��ʱ��Ϊ��һ�����̵����ʱ��
    int flag = 1;
    int sum = 0;
    printf("\n   ���Ƚ��̴��� ���еĽ��� ��ʼ����ʱ�� ����ʱ�� ʣ�����ʱ�� ����ʱ��\n");
    int z = 1;

    while (sum < n) {
        flag = 0;
        for (i = 1; i <= n; i++) {
            if (process[i].sc == 1) continue;   // ��������Ѿ���ɣ�������
            else {
                if (process[i].st1 <= T && time >= process[i].at) {   // �ж��Ƿ��ܹ�һ����������ý���
                    flag = 1;
                    time = time + process[i].st1;   // ����ʱ��
                    process[i].sc = 1;   // ��־�ý��������
                    process[i].ct = time;   // ��¼�ý������ʱ��
                    printf("Time:%-10d%-10d%-10d%-10d%-10d%-10d\n", z++, process[i].pn, time - process[i].st1,
                           process[i].st1, 0, time);    // ������̵�����Ϣ
                    process[i].st1 = 0;   // ����ʣ�����ʱ��Ϊ 0
                } else if (process[i].st1 > T && time >= process[i].at) {   // �ж��Ƿ���Ҫ�ֶ������
                    flag = 1;
                    time = time + T;    // ����ʱ��
                    process[i].st1 -= T;   // ����ʣ�����ʱ��
                    printf("Time:%-10d%-10d%-10d%-10d%-10d%-10d\n", z++, process[i].pn, time - T, T, process[i].st1,
                           time);   // ������̵�����Ϣ
                }
                if (process[i].sc == 1) sum++;   // ����ý�������ɣ����ۼ�����ɵĽ�������
            }
        }

        if (flag == 0 && sum < n) {
            for (i = 1; i <= n; i++)
                if (process[i].sc == 0) {
                    time = process[i].at;   // �����ǰû�н��̿��Ե��ȣ��л�����һ��δ��ɵĽ���
                    break;
                }
        }

    }
}

int main() {
    int n;
    printf("\t\tʱ��Ƭ��ת�����㷨\n");
    printf("�������ܽ�������\n");
    scanf("%d", &n);
    sjp(n);
    return 0;
}
