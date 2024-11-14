#include <stdio.h>

// 函数声明
void printArray(int arr[], int size);

void sortArray(int arr[], int size);

int main() {
    // 声明并初始化一个一维整型数组
    int arr[10] = {4, 8, 2, 10, 7, 1, 9, 3, 6, 5};

    // 打印数组元素
    printf("Original array:\n");
    printArray(arr, 10);

    // 对数组进行排序
    sortArray(arr, 10);

    // 打印排序后的数组元素
    printf("Sorted array:\n");
    printArray(arr, 10);

    return 0;
}

// 打印数组元素
void printArray(int arr[], int size) {
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

// 对数组进行排序（使用冒泡排序）
void sortArray(int arr[], int size) {
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                // 交换元素
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}