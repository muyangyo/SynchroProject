#define _CRT_SECURE_NO_WARNINGS 1
/*
 ×÷Õß£ºMuYang.
 »úÆ÷£ºDR
 ±àÒëÆ÷£ºVS2019
*/
void remove_common(char* delpos, int longth, int sub)
{
	for (int i = 0; i < longth - sub; i++)
	{
		delpos[i] = delpos[i + 1];
	}
}
int getlongth(char* parr)
{
	int longth = 0;
	while (*parr != '\0')
	{
		longth++;
		parr++;
	}
	return longth;
}

#include <stdio.h>
int main()
{
	char arr1[100] = { 0 };

	char arr2[100] = { 0 };
	char* pos2 = arr2;

	gets(arr1);
	gets(arr2);

	int longth = getlongth(arr1);

	while ((*pos2) != '\0')
	{
		char* pos1 = arr1;
		int sub = 0;
		while ((*pos1) != '\0')
		{
			if (*pos2 == *pos1)
			{
				remove_common(pos1, longth, sub);
				continue;
			}
			pos1++;
			sub++;
		}
		pos2++;
	}

	printf("%s", arr1);
	return 0;
}