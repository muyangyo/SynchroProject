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

int main() {
    int arr[] = {1, 0, 2, 0, 3, 4, 0, 5, 0, 6};
    int size = sizeof(arr) / sizeof(arr[0]);

    int nonZeroCount = countNonZeroElements(arr, size);
    printf("数组中非0元素的个数: %d\n", nonZeroCount);

    return 0;
}