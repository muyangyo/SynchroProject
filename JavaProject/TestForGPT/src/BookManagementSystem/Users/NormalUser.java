package BookManagementSystem.Users;

import BookManagementSystem.Books.BookList;
import BookManagementSystem.operations.*;

import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:48
 */
public class NormalUser extends User {
    OPeration[] operations = {new FindOp(), new BorrowOp(), new ReturnOp(), new ExitOp()};

    public NormalUser(String userName) {
        super(userName);
    }

    @Override
    public void showRole() {
        System.out.println("普通用户" + super.getUsername() + " 你好!");
    }

    @Override
    public void showMenu(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("0.查找图书");
            System.out.println("1.借阅图书");
            System.out.println("2.归还图书");
            System.out.println("3.退出系统");
            System.out.print("请选择你要进行的操作：");
            int choice = scanner.nextInt();
            operations[choice].work(bookList);
        }
    }
}
