#include <stdio.h>
#include <string.h>

int main() {
    // 声明两个字符串型数组
    char str1[] = "Hello, ";
    char str2[] = "World!";

    // 计算拼接后的字符串长度
    int len1 = strlen(str1);
    int len2 = strlen(str2);
    int total_len = len1 + len2;

    // 声明一个足够大的字符数组来存储拼接后的字符串
    char result[total_len + 1];  // +1 用于存储字符串结束符 '\0'

    // 拼接字符串
    strcpy(result, str1);
    strcat(result, str2);

    // 打印拼接后的字符串
    printf("Concatenated string: %s\n", result);

    return 0;
}