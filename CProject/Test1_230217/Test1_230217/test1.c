#define _CRT_SECURE_NO_WARNINGS 1
/*
 ���ߣ�MuYang.
 ������DR
 ��������VS2019
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
//	//��żbitλ������
//	//0000 0000 0000 0000 0000 0000 0101 0101
//	return 0;
//}

#include <stdio.h>
int main()
{
	char a = -1;
	//1000 0000 0000 0000 0000 0000 0000 0001 -- -1��ԭ��
	//1111 1111 1111 1111 1111 1111 1111 1110 -- -1�ķ���
	//1111 1111 1111 1111 1111 1111 1111 1111 -- -1�Ĳ���
	//1111 1111								  -- aʵ�ʴ������:�ض�����,ֵΪ-1

	unsigned char b = -1;
	//1000 0000 0000 0000 0000 0000 0000 0001 -- -1��ԭ��
	//1111 1111 1111 1111 1111 1111 1111 1110 -- -1�ķ���
	//1111 1111 1111 1111 1111 1111 1111 1111 -- -1�Ĳ���
	//1111 1111								  -- bʵ�ʴ������:�ض�����,ֵΪ255

	unsigned char c = a + b;
	//����Ҫ����
	//�Ȱ� a���з������ͽ�����������, bҲ����b���޷������ͽ�����������

	//  1111 1111 1111 1111 1111 1111 1111 1111 -- a�Ĳ���(����������)
	//  0000 0000 0000 0000 0000 0000 1111 1111 -- b�Ĳ���(����������)
	//1 0000 0000 0000 0000 0000 0000 1111 1110 -- c��ԭ��/����/����(����������ò�����м����)
	//  1111 1110							    -- cʵ�ʴ������:�ض�����,ֵΪ254

	printf("%d", c);
	//����������� �����з���ʮ���� ,������Ҫ��������
	//c���޷��ŵ�,ֱ�Ӳ�0,����ʽ
	//0000 0000 0000 0000 0000 0000 1111 1110 -- c��ԭ��/����/����
	//���� �����з���ʮ���� ����ʶ��,�ó�c=254

	return 0;
}