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