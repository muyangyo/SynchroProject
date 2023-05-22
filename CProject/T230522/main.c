#include <stdio.h>
#include <stdlib.h>

typedef struct page {
    int num;        // 页号
    int time;       // 等待时间，LRU算法会用到这个属性
} Page;

int pageNum;        // 系统分配给作业的主存中的页面数
int memoryNum;      // 可用内存页面数

void print(Page *memory);   // 打印当前主存中的页面
int searchPage(int page_num, Page *memory);    // 在页面集memory中查找page_num，如果找到，返回其在memory中的下标，否则返回-1


int main() {
    int i;
    int curMemory;      // 调入主存中的页面个数
    int missNum;        // 缺页次数
    float missRate;     // 缺页率
    char c;             // 得到用户的输入字符，来选择相应的置换算法

    Page *pages;        // 作业页面集
    Page *memory;       // 内存页面集

    printf("输入作业需要处理的页面数:");
    scanf("%d", &pageNum);
    printf("输入分配到的内存页面数:");
    scanf("%d", &memoryNum);

    pages = (Page *) malloc(sizeof(Page) * pageNum);
    memory = (Page *) malloc(sizeof(Page) * memoryNum);

    for (i = 0; i < pageNum; i++) {
        printf("第%d个页面号为: ", i);
        scanf("%d", &pages[i].num);
        pages[i].time = 0;      // 等待时间开始默认为0
    }

    printf("\n");
    printf("\n");

    do {
        for (i = 0; i < memoryNum; i++) {       // 初始化内存中页面
            memory[i].num = 0;                 // 页面为空用0表示
            memory[i].time = -1;                //
        }
        printf("--------------------------------\n");
        printf("     f: FIFO页面置换\n");
        printf("     o: OPT页面置换\n");
        printf("     l: LRU页面置换\n");
        printf("--请选择操作类型(f, o, l),按其它键结束--\n");
        fflush(stdin);
        scanf("%c", &c);

        i = 0;
        curMemory = 0;

        if (c == 'f') {         // FIFO页面置换
            missNum = 0;

            printf("FIFO页面置换情况:\n");
            for (i = 0; i < pageNum; i++) {
                if (searchPage(pages[i].num, memory) < 0) {   // 若在主存中没有找到该页面
                    missNum++;
                    memory[curMemory].num = pages[i].num;
                    print(memory);
                    curMemory = (curMemory + 1) % memoryNum;
                }
            }   // end for
            missRate = (float) missNum / pageNum;
            printf("缺页次数：%d   缺页率:  %f\n", missNum, missRate);

        }//end if

        if (c == 'o') {         // OPT页面置换
            missNum = 0;
            printf("OPT页面置换情况:\n");
            for (i = 0; i < pageNum; i++) {
                if (searchPage(pages[i].num, memory) < 0) {   // 若在主存中没有找到该页面
                    missNum++;
                    int replaceIndex = -1;
                    int maxDistance = -1;
                    int j, k;

                    // 寻找最长时间未被访问的页面
                    for (j = 0; j < memoryNum; j++) {
                        int distance = -1;
                        for (k = i + 1; k < pageNum; k++) {
                            if (memory[j].num == pages[k].num) {
                                distance = k - i;
                                break;
                            }
                        }
                        if (distance == -1) {   // 如果该页面以后不再被访问，直接替换
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
            printf("缺页次数：%d   缺页率:  %f\n", missNum, missRate);
        }// end if

        if (c == 'l') {         // LRU页面置换
            missNum = 0;
            printf("LRU页面置换情况:\n");
            for (i = 0; i < pageNum; i++) {
                if (searchPage(pages[i].num, memory) < 0) {   // 若在主存中没有找到该页面
                    missNum++;

                    int replaceIndex = -1;
                    int maxTime = -1;
                    int j, k;

                    // 寻找最近最久未使用的页面
                    for (j = 0; j < memoryNum; j++) {
                        int time = -1;
                        for (k = 0; k < i; k++) {
                            if (memory[j].num == pages[k].num) {
                                time = i - k;
                                break;
                            }
                        }
                        if (time == -1) {   // 如果该页面都没被使用过，直接替换
                            replaceIndex = j;
                            break;
                        } else if (time > maxTime) {
                            maxTime = time;
                            replaceIndex = j;
                        }
                    }

                    memory[replaceIndex].num = pages[i].num;
                    print(memory);

                    // 更新每个页面的等待时间
                    for (j = 0; j < memoryNum; j++) {
                        if (j != replaceIndex) {
                            memory[j].time++;
                        } else {
                            memory[j].time = 0;
                        }
                    }
                } else {    // 如果在主存中找到该页面，更新其等待时间
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
            printf("缺页次数：%d   缺页率:  %f\n", missNum, missRate);
        }   // end if

    } while (c == 'f' || c == 'l' || c == 'o');

    free(pages);
    free(memory);

    return 0;
}

void print(Page *memory) {  // 打印当前的页面
    int j;

    for (j = 0; j < memoryNum; j++) {
        printf("%2d ", memory[j].num);
    }
    printf("\n");
}

// 在页面集memory中查找page_num，如果找到，返回其在memory中的下标，否则返回-1
int searchPage(int page_num, Page *memory) {
    for (int i = 0; i < memoryNum; i++) {
        if (memory[i].num == page_num) {
            return i;
        }
    }
    return -1;
}
