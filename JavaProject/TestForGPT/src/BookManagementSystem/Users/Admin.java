package BookManagementSystem.Users;

import BookManagementSystem.Books.BookList;
import BookManagementSystem.operations.*;

import java.util.Scanner;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: DR
 * Date: 2023/3/23
 * Time: 23:47
 */
public class Admin extends User {
    OPeration[] operations = {new FindOp(), new AddOp(), new DelOp(), new ShowOp(), new ExitOp()};

    public Admin(String userName) {
        super(userName);
    }


    @Override
    public void showRole() {
        System.out.println("管理员:" + super.getUsername() + " 你好!");
    }

    @Override
    public void showMenu(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("0.查找图书");
            System.out.println("1.新增图书");
            System.out.println("2.删除图书");
            System.out.println("3.显示图书");
            System.out.println("4.退出系统");
            System.out.print("请选择你要进行的操作：");
            int choice = scanner.nextInt();
            operations[choice].work(bookList);
        }
    }
}
