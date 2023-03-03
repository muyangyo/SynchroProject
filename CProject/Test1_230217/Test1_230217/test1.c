#define _CRT_SECURE_NO_WARNINGS 1
/*
 作者：MuYang.
 机器：DR
 编译器：VS2019
*/
//#include <stdio.h>
//#include <string.h>
//void remove_common(char* delpos, int longth, int sub)
//{
//	for (int i = 0; i < longth - sub; i++)
//	{
//		delpos[i] = delpos[i + 1];
//	}
//}
//
//int main()
//{
//	char arr1[100] = { 0 };
//
//	char arr2[100] = { 0 };
//	char* pos2 = arr2;
//
//	gets(arr1);
//	gets(arr2);
//
//	int longth = strlen(arr1);
//
//	while ((*pos2) != '\0')
//	{
//		char* pos1 = arr1;
//		int sub = 0;
//		while ((*pos1) != '\0')
//		{
//			if (*pos2 == *pos1)
//			{
//				remove_common(pos1, longth, sub);
//				continue;
//			}
//			pos1++;
//			sub++;
//		}
//		pos2++;
//	}
//
//	printf("%s", arr1);
//	return 0;
//}
//#include <stdio.h>
//#define N 4
//
//#define Y(n) ((N+2)*n)
//int main()
//{
//	int z = 2 * (N + Y(5 + 1));
//	printf("%d", z);
//	return 0;
//}



//#include <stdio.h>
//typedef struct MyStruct
//{
//	int a;
//	char b;
//	int c;
//}MyStruct;
//
//#define OFFSETOF(Type,Mumber) (int)( &( ( (Type*)0 )->Mumber ) )

//int main()
//{
//	//int offset = OFFSETOF(MyStruct, c);
//	//printf("%d", offset);
//	int* p = *(int*)0;
//	return 0;
//}

//#define A 100;

//#define A 100

//#define B (5+5)

//#include <stdio.h>
//
//#define EPB(x) ((x&0xAAAAAAAA)>>1)+((x&(0xAAAAAAAA>>1))>>1)
//
//int main()
//{
//	int a = 170;
//	//0000 0000 0000 0000 0000 0000 1010 1010
//	int b = EPB(a);
//	//奇偶bit位交换后
//	//0000 0000 0000 0000 0000 0000 0101 0101
//	return 0;
//}

#include <stdio.h>
int main()
{
	char a = -1;
	//1000 0000 0000 0000 0000 0000 0000 0001 -- -1的原码
	//1111 1111 1111 1111 1111 1111 1111 1110 -- -1的反码
	//1111 1111 1111 1111 1111 1111 1111 1111 -- -1的补码
	//1111 1111								  -- a实际存的数据:截断数据,值为-1

	unsigned char b = -1;
	//1000 0000 0000 0000 0000 0000 0000 0001 -- -1的原码
	//1111 1111 1111 1111 1111 1111 1111 1110 -- -1的反码
	//1111 1111 1111 1111 1111 1111 1111 1111 -- -1的补码
	//1111 1111								  -- b实际存的数据:截断数据,值为255

	unsigned char c = a + b;
	//由于要计算
	//先按 a的有符号类型进行整形提升, b也按照b的无符号类型进行整形提升

	//  1111 1111 1111 1111 1111 1111 1111 1111 -- a的补码(整形提升后)
	//  0000 0000 0000 0000 0000 0000 1111 1111 -- b的补码(整形提升后)
	//1 0000 0000 0000 0000 0000 0000 1111 1110 -- c的原码/反码/补码(计算机都是用补码进行计算的)
	//  1111 1110							    -- c实际存的数据:截断数据,值为254

	printf("%d", c);
	//这里输出的是 整形有符号十进制 ,所以需要整形提升
	//c是无符号的,直接补0,得下式
	//0000 0000 0000 0000 0000 0000 1111 1110 -- c的原码/反码/补码
	//按照 整形有符号十进制 进行识别,得出c=254

	return 0;
}