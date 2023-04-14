#include <stdio.h>
#include <conio.h>

// �ź������Ͷ���Ϊ int ����
typedef int semaphore;

// ���̽ṹ�壬��ʱΪ��
struct process {
};

// �������ṹ��
typedef struct {
    // ��Դ�ź�������ʾ���Դ�����ݵĻ���������
    int resource_count;
    // ָ����̵�ָ��
    struct process *process_ptr;
} buffer;

// P������������Դ��n����Դ�ź���ָ��
void acquire_resource(int *n) {
    (*n)--;   // ����һ����Դ
    if ((*n) < 0) {
        printf("û�п�����Դ����ȴ�...\n");
    } else {
        printf("�ɹ����뵽һ����Դ����ǰ������Դ����: %d\n", (*n));
    }
}

// V�������ͷ���Դ��n����Դ�ź���ָ��
void release_resource(int *n) {
    (*n)++;   // �ͷ�һ����Դ

    if ((*n) <= 0) {
        printf("�����������̱����������ڻ�������һ������...\n");
    } else {
        printf("�ɹ��ͷ�һ����Դ����ǰ������Դ����: %d\n", (*n));
    }
}

// �����ߺ�����mutex�ǻ����ź���ָ�룬full��empty����Դ�ź���ָ��
void producer(int *mutex, int *full, int *empty) {
    printf("�����߽���������...\n\n");
    while (1) {
        printf("��������һ��������...\n");
        acquire_resource(empty);  // ��ÿ��л�������Ԫ ���Ȼ����Դ
        acquire_resource(mutex);  // �ٻ�ý����ٽ������ʸ�
        printf("�ɹ�����������뻺������.\n"); // �����ݷ��뻺����
        release_resource(mutex);  // �뿪�ٽ���
        release_resource(full);  // ��������
        getch();
    }
}

// �����ߺ�����mutex�ǻ����ź���ָ�룬full��empty����Դ�ź���ָ��
void consumer(int *mutex, int *full, int *empty) {
    printf("�����߽���������...\n\n");
    while (1) {
        acquire_resource(full);  // �������������Ԫ ���Ȼ����Դ
        acquire_resource(mutex);  // �ٻ�ý����ٽ������ʸ�
        printf("�ɹ��ӻ�����ȡ��һ��������.\n"); // �ӻ�����ȡ������
        release_resource(mutex);  // �뿪�ٽ���
        release_resource(empty);  // ��������
        printf("�ɹ�������������.\n");// ��������
        getch();
    }
}

int main() {
    // ���������ź����������ź��������л������ź��������������ź���
    semaphore mutex = 1;
    semaphore empty = 3;
    semaphore full = 0;

    // ���������ߺ����������ߺ���
    producer(&mutex, &full, &empty);
    consumer(&mutex, &full, &empty);

    return 0;
}
