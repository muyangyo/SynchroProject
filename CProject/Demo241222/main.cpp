#include <stdio.h>

void doubleArrayElements(int *arr, int size) {
    int *ptr = arr;
    while (ptr < arr + size) {
        *ptr = *ptr * 2;
        ptr++;
    }
}

int main() {
    int array[15];
    int i;

    printf("请输入15个整数：\n");
    for (i = 0; i < 15; i++) {
        scanf("%d", &array[i]);
    }

    doubleArrayElements(array, 15);

    printf("扩大2倍后的数组元素：\n");
    for (i = 0; i < 15; i++) {
        printf("%d ", array[i]);
    }
    printf("\n");

    return 0;
}

