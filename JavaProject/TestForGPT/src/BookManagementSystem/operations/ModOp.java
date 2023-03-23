package BookManagementSystem.operations;

import BookManagementSystem.Books.Book;
import BookManagementSystem.Books.BookList;

import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:47
 */
public class ModOp implements OPeration {

    @Override
    public void work(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入需要修改的书名: ");
        String BookName = scanner.nextLine();
        int index = -1;
        for (int i = 0; i < bookList.Books.length; i++) {
            if (bookList.Books[i].getBookName().equals(BookName)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            System.out.println("未找到该图书");
            return;
        }
        System.out.print("请输入编号：");
        String SN = scanner.nextLine();
        System.out.print("请输入书名：");
        String bookName = scanner.nextLine();
        System.out.print("请输入作者：");
        String author = scanner.nextLine();
        System.out.print("请输入价格：");
        double price = scanner.nextDouble();
        Book book = new Book(SN, bookName, author, price);
        bookList.Books[index] = book;
        System.out.println("修改成功!");
    }
}
