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
public class FindOp implements OPeration {

    @Override
    public void work(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入需要查找的书名: ");
        String BookName = scanner.nextLine();
        for (int i = 0; i < bookList.UsedSize; i++) {
            if (bookList.Books[i].getBookName().equals(BookName)) {
                System.out.println(bookList.Books[i]);
                return;
            }
        }
        System.out.println("未找到该图书");
    }
}
