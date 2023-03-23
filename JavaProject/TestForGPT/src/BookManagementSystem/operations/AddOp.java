package BookManagementSystem.operations;

import BookManagementSystem.Books.Book;
import BookManagementSystem.Books.BookList;

import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:12
 */
public class AddOp implements OPeration {
    @Override
    public void work(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        if (bookList.UsedSize == bookList.Books.length) {
            Book[] newBooks = new Book[bookList.Books.length + 2];
            System.arraycopy(bookList.Books, 0, newBooks, 0, bookList.Books.length);
            bookList.Books = newBooks;
        }
        System.out.print("请输入编号：");
        String SN = scanner.nextLine();
        System.out.print("请输入书名：");
        String BookName = scanner.nextLine();
        System.out.print("请输入作者：");
        String author = scanner.nextLine();
        System.out.print("请输入价格：");
        double price = scanner.nextDouble();

        Book book = new Book(SN, BookName, author, price);
        bookList.Books[bookList.UsedSize] = book;
        bookList.UsedSize++;
        System.out.println("添加成功!");
    }
}

