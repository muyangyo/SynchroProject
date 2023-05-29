import pandas as pd

# 读取Excel文件
data = pd.read_excel('C:/Users/DR/Desktop/1.xlsx')

# 检查所需列是否存在于DataFrame中
required_cols = ['题干', '答案']
missing_cols = set(required_cols) - set(data.columns)
if missing_cols:
    raise ValueError(f"数据文件缺少必要的列: {missing_cols}")


# 模糊搜索B列并返回所有匹配结果的B列和H列数据
def find_string(string_to_find):
    result = []
    for index, row in data.iterrows():
        if string_to_find in str(row['题干']):
            result.append((row['题干'], row['答案']))

    return result


# 循环输入并查询字符串
while True:
    string_to_find = input('请输入要查找的字符串（输入 q 退出）：')
    if string_to_find == 'q':
        break
    results = find_string(string_to_find)
    if len(results) > 0:
        print(f'包含关键字"{string_to_find}"的行数为：{len(results)}')
        for r in results:
            print(f'题干: {r[0]}\n答案: {r[1]}')
    else:
        print(f'未找到包含关键字"{string_to_find}"的数据')
