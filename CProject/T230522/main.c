#include <stdio.h>
#include <stdlib.h>

typedef struct page {
    int num;        // ҳ��
    int time;       // �ȴ�ʱ�䣬LRU�㷨���õ��������
} Page;

int pageNum;        // ϵͳ�������ҵ�������е�ҳ����
int memoryNum;      // �����ڴ�ҳ����

void print(Page *memory);   // ��ӡ��ǰ�����е�ҳ��
int searchPage(int page_num, Page *memory);    // ��ҳ�漯memory�в���page_num������ҵ�����������memory�е��±꣬���򷵻�-1


int main() {
    int i;
    int curMemory;      // ���������е�ҳ�����
    int missNum;        // ȱҳ����
    float missRate;     // ȱҳ��
    char c;             // �õ��û��������ַ�����ѡ����Ӧ���û��㷨

    Page *pages;        // ��ҵҳ�漯
    Page *memory;       // �ڴ�ҳ�漯

    printf("������ҵ��Ҫ�����ҳ����:");
    scanf("%d", &pageNum);
    printf("������䵽���ڴ�ҳ����:");
    scanf("%d", &memoryNum);

    pages = (Page *) malloc(sizeof(Page) * pageNum);
    memory = (Page *) malloc(sizeof(Page) * memoryNum);

    for (i = 0; i < pageNum; i++) {
        printf("��%d��ҳ���Ϊ: ", i);
        scanf("%d", &pages[i].num);
        pages[i].time = 0;      // �ȴ�ʱ�俪ʼĬ��Ϊ0
    }

    printf("\n");
    printf("\n");

    do {
        for (i = 0; i < memoryNum; i++) {       // ��ʼ���ڴ���ҳ��
            memory[i].num = 0;                 // ҳ��Ϊ����0��ʾ
            memory[i].time = -1;                //
        }
        printf("--------------------------------\n");
        printf("     f: FIFOҳ���û�\n");
        printf("     o: OPTҳ���û�\n");
        printf("     l: LRUҳ���û�\n");
        printf("--��ѡ���������(f, o, l),������������--\n");
        fflush(stdin);
        scanf("%c", &c);

        i = 0;
        curMemory = 0;

        if (c == 'f') {         // FIFOҳ���û�
            missNum = 0;

            printf("FIFOҳ���û����:\n");
            for (i = 0; i < pageNum; i++) {
                if (searchPage(pages[i].num, memory) < 0) {   // ����������û���ҵ���ҳ��
                    missNum++;
                    memory[curMemory].num = pages[i].num;
                    print(memory);
                    curMemory = (curMemory + 1) % memoryNum;
                }
            }   // end for
            missRate = (float) missNum / pageNum;
            printf("ȱҳ������%d   ȱҳ��:  %f\n", missNum, missRate);

        }//end if

        if (c == 'o') {         // OPTҳ���û�
            missNum = 0;
            printf("OPTҳ���û����:\n");
            for (i = 0; i < pageNum; i++) {
                if (searchPage(pages[i].num, memory) < 0) {   // ����������û���ҵ���ҳ��
                    missNum++;
                    int replaceIndex = -1;
                    int maxDistance = -1;
                    int j, k;

                    // Ѱ���ʱ��δ�����ʵ�ҳ��
                    for (j = 0; j < memoryNum; j++) {
                        int distance = -1;
                        for (k = i + 1; k < pageNum; k++) {
                            if (memory[j].num == pages[k].num) {
                                distance = k - i;
                                break;
                            }
                        }
                        if (distance == -1) {   // �����ҳ���Ժ��ٱ����ʣ�ֱ���滻
                            replaceIndex = j;
                            break;
                        } else if (distance > maxDistance) {
                            maxDistance = distance;
                            replaceIndex = j;
                        }
                    }

                    memory[replaceIndex].num = pages[i].num;
                    print(memory);
                }
            }   //end for
            missRate = (float) missNum / pageNum;
            printf("ȱҳ������%d   ȱҳ��:  %f\n", missNum, missRate);
        }// end if

        if (c == 'l') {         // LRUҳ���û�
            missNum = 0;
            printf("LRUҳ���û����:\n");
            for (i = 0; i < pageNum; i++) {
                if (searchPage(pages[i].num, memory) < 0) {   // ����������û���ҵ���ҳ��
                    missNum++;

                    int replaceIndex = -1;
                    int maxTime = -1;
                    int j, k;

                    // Ѱ��������δʹ�õ�ҳ��
                    for (j = 0; j < memoryNum; j++) {
                        int time = -1;
                        for (k = 0; k < i; k++) {
                            if (memory[j].num == pages[k].num) {
                                time = i - k;
                                break;
                            }
                        }
                        if (time == -1) {   // �����ҳ�涼û��ʹ�ù���ֱ���滻
                            replaceIndex = j;
                            break;
                        } else if (time > maxTime) {
                            maxTime = time;
                            replaceIndex = j;
                        }
                    }

                    memory[replaceIndex].num = pages[i].num;
                    print(memory);

                    // ����ÿ��ҳ��ĵȴ�ʱ��
                    for (j = 0; j < memoryNum; j++) {
                        if (j != replaceIndex) {
                            memory[j].time++;
                        } else {
                            memory[j].time = 0;
                        }
                    }
                } else {    // ������������ҵ���ҳ�棬������ȴ�ʱ��
                    int index = searchPage(pages[i].num, memory);
                    memory[index].time = 0;
                    for (int j = 0; j < memoryNum; j++) {
                        if (j != index) {
                            memory[j].time++;
                        }
                    }
                }
            }   //end for
            missRate = (float) missNum / pageNum;
            printf("ȱҳ������%d   ȱҳ��:  %f\n", missNum, missRate);
        }   // end if

    } while (c == 'f' || c == 'l' || c == 'o');

    free(pages);
    free(memory);

    return 0;
}

void print(Page *memory) {  // ��ӡ��ǰ��ҳ��
    int j;

    for (j = 0; j < memoryNum; j++) {
        printf("%2d ", memory[j].num);
    }
    printf("\n");
}

// ��ҳ�漯memory�в���page_num������ҵ�����������memory�е��±꣬���򷵻�-1
int searchPage(int page_num, Page *memory) {
    for (int i = 0; i < memoryNum; i++) {
        if (memory[i].num == page_num) {
            return i;
        }
    }
    return -1;
}
