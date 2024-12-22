#include <stdio.h>

// 统计一维数组中非0元素的个数的函数
int countNonZeroElements(int arr[], int size) {
    int count = 0;
    for (int i = 0; i < size; i++) {
        if (arr[i] != 0) {
            count++;
        }
    }
    return count;
}

int main1() {
    int arr[] = {1, 0, 2, 0, 3, 4, 0, 5, 0, 6};
    int size = sizeof(arr) / sizeof(arr[0]);

    int nonZeroCount = countNonZeroElements(arr, size);
    printf("数组中非0元素的个数: %d\n", nonZeroCount);

    return 0;
}


// 函数声明，用于将数组元素扩大2倍
void doubleArrayElements(int *arr, int size);

int main() {
    int array[15];
    int i;

    // 输入数组元素
    printf("请输入15个整数：\n");
    for (i = 0; i < 15; i++) {
        scanf("%d", &array[i]);
    }

    // 调用函数，将数组元素扩大2倍
    doubleArrayElements(array, 15);

    // 输出扩大后的数组元素
    printf("扩大2倍后的数组元素：\n");
    for (i = 0; i < 15; i++) {
        printf("%d ", array[i]);
    }
    printf("\n");

    return 0;
}

// 函数定义，使用指针将数组元素扩大2倍
void doubleArrayElements(int *arr, int size) {
    int *ptr = arr; // 指针ptr初始化为数组的首地址
    while (ptr < arr + size) { // 遍历数组直到最后一个元素
        *ptr = *ptr * 2; // 将当前元素扩大2倍
        ptr++; // 移动到下一个元素
    }
}