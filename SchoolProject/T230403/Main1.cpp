//
// Created by DR on 2023/4/3.
//
#include<iostream>
#include<assert.h>
#include<stdlib.h>

using namespace std;

enum Process_State {
    Pro_State_blocked = 1,
    Pro_State_running = 2,
};
typedef struct PCB {
    int id;
    int size;
    char content[20];
    int state;
    struct PCB *next;
} PCB;
PCB *running_list = (PCB *) malloc(sizeof(PCB));
PCB *blocked_list = (PCB *) malloc(sizeof(PCB));

void create(PCB *running_list, PCB *blocked_list, int *size);

void show(PCB *running_list);

void change(PCB *running_list, PCB *blocked_list, int *size);

void kill(PCB *running_list, int *size);

void awake(PCB *running_list, int *blocked_list, int *size);

int isExist_running(PCB *running_list, int id);

int isExist_blocked(PCB *running_list, int id);

PCB *find(PCB *list, int id);

void create(PCB *running_list, PCB *blocked_list, int *size) {
    PCB *pro = (PCB *) malloc(sizeof(PCB));
    assert(pro != NULL);
    int id;
    cout << "请输入进程名,判断是否重复" << endl;
    cin >> id;

    if (isExist_running(running_list, id)) {
        cout << "进程重名了,换个名字继续吧" << endl;
        return;
    }
    if (isExist_blocked(blocked_list, id)) {
        cout << "进程重名啦,换个名字继续吧" << endl;
        return;
    }
    pro->id = id;
    cout << "请输入进程的大小" << endl;
    cin >> pro->size;
    if (pro->size >= 1024) {
        cout << "内存不足,换个小一点的程序吧" << endl;
        return;
    }
    cout << "请输入进程的内容" << endl;
    cin >> pro->content;
    pro->state = Pro_State_running;
    pro->next = NULL;
    PCB *npro = running_list;
    while (npro->next != NULL) {
        npro = npro->next;
    }
    npro->next = pro;
    *size = *size + 1;

    cout << "进程创建成功" << endl;

}

void show(PCB *running_list) {
    PCB *npro = running_list->next;
    if (npro == NULL) {
        cout << "当前没有正在运行的进程" << endl;
        return;
    }
    cout << "正在运行的进程有:" << endl;
    while (npro != NULL) {
        cout << "进程名: " << npro->id << endl;
        cout << "进程大小: " << npro->size << endl;
        cout << "进程内容: " << npro->content << endl;
        if (npro->next != NULL) {
            cout << "------------" << endl;
        }
        npro = npro->next;
    }
}

void change(PCB *running_list, PCB *blocked_list, int *size) {
    if (*size == 0) {
        cout << "当前没有正在运行的进程" << endl;
        return;
    }
    int id;
    cout << "请输入需要阻塞的进程名字:";
    cin >> id;
    if (isExist_running(running_list, id)) {
        PCB *npro = find(running_list, id);
        npro->next->state = Pro_State_blocked;

        PCB *pro = blocked_list;
        while (pro->next != NULL) {
            pro = pro->next;
        }
        pro->next = npro->next;
        npro->next = npro->next->next;
        pro->next->next = NULL;
        *size = *size - 1;
        cout << "已经将进程换到阻塞队列" << endl;
    } else
        cout << "改进程不存在或者已经在就绪队列中" << endl;
}

void kill(PCB *running_list, int *size) {
    if (*size == 0) {
        cout << "没有可以杀死的进程" << endl;
        return;
    }
    int id;
    cout << "请输入要杀死的进程:" << endl;
    cin >> id;
    if (isExist_running(running_list, id)) {
        PCB *npro = find(running_list, id);
        PCB *location = npro->next;
        npro->next = npro->next->next;
        *size = *size - 1;
        free(location);
        cout << "已经杀死该进程" << endl;
    } else
        cout << "该进程不存在或者已处于阻塞队列中" << endl;
}

void awake(PCB *running_list, PCB *blocked_list, int *size) {
    PCB *npro = blocked_list;
    if (npro->next == NULL) {
        cout << "没有可以唤醒的进程" << endl;
        return;
    }
    int id;
    cout << "请输入要唤醒的进程名字:" << endl;
    cin >> id;
    if (isExist_blocked(blocked_list, id)) {
        npro = find(blocked_list, id);
        npro->next->state = Pro_State_running;
        PCB *pro = running_list;
        while (pro->next != NULL) {
            pro = pro->next;
        }
        pro->next = npro->next;
        npro->next = npro->next->next;
        pro->next->next = NULL;
        *size = *size + 1;
        cout << "已成功唤醒进程" << endl;
    } else
        cout << "该进程不存在或已在运行队列中" << endl;
}

int isExist_running(PCB *running_list, int id) {
    int result = 0;
    PCB *pro = running_list->next;
    while (pro != NULL) {
        if (pro->id == id) {
            result = 1;
            break;
        }
        pro = pro->next;
    }
    return result;
}

int isExist_blocked(PCB *blocked_list, int id) {
    int result = 0;
    PCB *pro = blocked_list->next;
    while (pro != NULL) {
        if (pro->id == id) {
            result = 1;
            break;
        }
        pro = pro->next;
    }
    return result;
}

PCB *find(PCB *list, int id) {
    PCB *pro = list;
    while (pro->next != NULL) {
        if (pro->next->id == id) {
            return pro;
        }

        pro = pro->next;
    }
    return NULL;
}

int main() {
    running_list->next = NULL;
    blocked_list->next = NULL;
    int pro_size = 0;
    char choose;
    while (1) {
        cout << "欢迎来到我的进程控制系统" << endl;
        cout << "1-创建新进程" << endl;
        cout << "2-展示当前进程" << endl;
        cout << "3-阻塞执行进程" << endl;
        cout << "4-唤醒阻塞进程" << endl;
        cout << "5-终止执行程序" << endl;
        cout << "6-退出程序" << endl;
        cout << "请输入对应的编号来执行相应的操作" << endl;

        cin >> choose;
        switch (choose) {
            case '1':
                create(running_list, blocked_list, &pro_size);
                break;
            case '2':
                show(running_list);
                break;
            case '3':
                change(running_list, blocked_list, &pro_size);
                break;
            case '4':
                awake(running_list, blocked_list, &pro_size);
                break;
            case '5':
                kill(running_list, &pro_size);
                break;
            case '6':
                return 0;
            default:
                cout << "出错啦,请输入序号" << endl;
                break;
        }
        cout << "------------------------------" << endl;
    }
}
