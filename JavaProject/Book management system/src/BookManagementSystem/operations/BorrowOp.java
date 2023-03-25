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
public class BorrowOp implements OPeration {

    @Override
    public void work(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入需要借出的书名: ");
        String BookName = scanner.nextLine();
        for (int i = 0; i < bookList.Books.length; i++) {
            if (bookList.Books[i].getBookName().equals(BookName) && bookList.Books[i].getBorrowed()) {
                System.out.println("成功借阅:" + bookList.Books[i]);
                bookList.Books[i].setBorrowed(true);
                return;
            }
        }
        System.out.println("未找到该图书");
    }
}
