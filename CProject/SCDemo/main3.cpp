#include <stdio.h>

int main() {
    // 声明并初始化一个二维整型数组
    int arr[3][3] = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };

    // 计算主对角线和副对角线上的数之和
    int sum_main_diagonal = 0;
    int sum_secondary_diagonal = 0;

    for (int i = 0; i < 3; i++) {
        sum_main_diagonal += arr[i][i];
        sum_secondary_diagonal += arr[i][2 - i];
    }

    // 打印结果
    printf("Sum of main diagonal: %d\n", sum_main_diagonal);
    printf("Sum of secondary diagonal: %d\n", sum_secondary_diagonal);

    return 0;
}