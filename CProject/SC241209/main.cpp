#include <stdio.h>

unsigned long long factorial(int n);

int main1() {
    unsigned long long sum = 0;
    int i;

    for (i = 1; i <= 10; i++) {
        sum += factorial(i);
    }

    printf("1!+2!+3!+...+10! = %llu\n", sum);
    return 0;
}

unsigned long long factorial(int n) {
    if (n == 0 || n == 1) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
}