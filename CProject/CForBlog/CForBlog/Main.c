#define _CRT_SECURE_NO_WARNINGS 1
/*
 ×÷Õß£ºMuYang.
 »úÆ÷£ºDR
 ±àÒëÆ÷£ºVS2019
*/
#include <stdio.h>
//int main()
//{
//	char arr[20] = "";
//	gets("%s", arr);
//	printf("%s\n", arr);
//	return 0;
//}

int a = 10;
void test()
{
	int a = 88;
	printf("%d\n", a);
	a = 99;
	printf("%d\n", a);
}
void test2()
{
	static int s1 = 0;
	s1++;
	printf("%d\n", s1);
}
int main()
{
	test();
	test2();
	test2();

	int flag = 2;
	while (flag)
	{
		int b = 2;
		b++;
		flag--;
	}
	printf("%d\n", a);
	//printf("%d", b);
	return 0;
}
