#include <stdio.h>
#define N 50
struct PCB
{
    int pn;   //process name进程名字
    int at;   //arrival time到达时间
    int st;   //service time服务时间
    int ct;   //completion time完成时刻
    int sc;  //sign completion标志是否完成
    int st1;  //剩余服务时间
}process[N];

void sjp(int n)
{
    int i,j,T;
    printf("\n请输入时间片：\n");
    scanf("%d",&T);
    for(i=1;i<=n;i++)      //收集进程信息
    {
        process[i].sc=0;
        printf("\n%d:\n请依次输入进程的信息\n请输入pn:",i);
        scanf("%d",&process[i].pn);
        printf("请输入at:");
        scanf("%d",&process[i].at);
        printf("请输入st:");
        scanf("%d",&process[i].st);
        process[i].st1=process[i].st;
    }
    for(i=1;i<=n;i++)
    for(j=i+1;j<=n;j++)   //按照各进程到达时间升序，对进程排序 注意：稳定的排序
    {
        if(process[j].at<process[i].at)
        {
            process[0]=process[j];
            process[j]=process[i];
            process[i]=process[0];
        }
    }
    //for(i=1;i<=n;i++)    //检查排序是否正确
    //printf("%d\t",process[i].pn);

    int time=process[1].at;      //当前时间的初值
    int flag=1;
    int sum=0;                  //记录完成的进程数
    printf("\n第几次调度进程 运行的进程pn 开始运行时间 运行时间 剩余服务时间 结束时间\n");
    int z=1;   //记录第几次调度进程

    while(sum<n)
    {
        flag=0;         //标志就绪队列中是否还有进程
        for(i=1to n;i++)    //时间片轮转法执行各进程
        {
            if(process[i].sc==1) continue;  //已完成的进程
            else
             {
                if(process[i].st1<=T&&time>=process[i].at)//未完成的进程但是还需服务的时间少于等于一个时间片
                {
                flag=1;
                time=time+process[i].st1;
                process[i].sc=1;
                process[i].ct=time;
                printf("%-10d%-10d%-10d%-10d%-10d%-10d\n",z++,process[i].pn,time-process[i].st1,process[i].st1,0,time);
                process[i].st1=0;
                }

                else if(process[i].st1>T&&time>=process[i].at)//未完成的进程但其还需服务时间至少大于一个时间片
                {
                    flag=1;
                    time=time+T;
                    process[i].st1-=T;
                    printf("%-10d%-10d%-10d%-10d%-10d%-10d\n",z++,process[i].pn,time-T,T,process[i].st1,time);
                }
                if(process[i].sc==1) sum++;     //一个进程执行完就+1
            }
        }

        if(flag==0&&sum<n)   // 还有没执行的进程，且没进入就就绪队列
        {
        for(i=1;i<=n;i++)
        if(process[i].sc==0) {time=process[i].at;break;}
        }
    }
}

int main()
{
    int n;
    printf("\t\t时间片轮转调度算法\n");
    printf("请输入总进程数：\n");
    scanf("%d",&n);
    sjp(n);
    return 0;
}
