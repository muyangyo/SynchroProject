#include <iostream>
#include <stdio.h>


int main() {
    int num1, num2;
    int sum, product;

    printf("first Number:");
    scanf("%d", &num1);
    printf("second Number:");
    scanf("%d", &num2);

    sum = num1 + num2;
    product = num1 * num2;
    printf("sum: %d\n", sum);
    printf("product: %d\n", product);
    return 0;
}
