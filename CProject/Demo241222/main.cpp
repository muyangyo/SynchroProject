#include <stdio.h>

int findMax(int *arr, int size) {
    int max = *arr;

    for (int i = 1; i < size; i++) {
        if (*(arr + i) > max) {
            max = *(arr + i);
        }
    }

    return max;
}

int main() {
    int arr[] = {3, 7, 2, 9, 4, 5};
    int size = sizeof(arr) / sizeof(arr[0]);

    int max = findMax(arr, size);

    printf("最大值为: %d\n", max);

    return 0;
}