package BookManagementSystem.operations;

import BookManagementSystem.Books.BookList;

import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:47
 */
public class DelOp implements OPeration {

    @Override
    public void work(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入需要删除的书名: ");
        String BookName = scanner.nextLine();
        int index = -1;
        for (int i = 0; i < bookList.UsedSize; i++) {

            if (bookList.Books[i].getBookName().equals(BookName)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("未找到该图书");
            return;
        }
        for (int i = index; i < bookList.UsedSize - 1; i++) {
            bookList.Books[i] = bookList.Books[i + 1];
        }
        bookList.Books[bookList.UsedSize - 1] = null;
        bookList.UsedSize--;
        System.out.println("删除成功!");
    }
}
